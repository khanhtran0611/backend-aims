package Project_ITSS.PlaceOrder.Controller;

import Project_ITSS.PlaceOrder.DTO.*;
import Project_ITSS.PlaceOrder.Entity.*;
import Project_ITSS.PlaceOrder.Exception.PlaceOrderException;
import Project_ITSS.PlaceOrder.Service.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@NoArgsConstructor
public class PlaceOrderController {

    @Value("${calculating_service_version}")
    private String calculating_service_version;

    private final Map<String,IDeliveryFeeCalculating> DeliveryFeeCalculatingsMap = new HashMap<>();

    @Autowired
    PlaceOrderController(List<IDeliveryFeeCalculating> iDeliveryFeeCalculatings){
        for(IDeliveryFeeCalculating service : iDeliveryFeeCalculatings){
            DeliveryFeeCalculatingsMap.put(service.getVersion(),service);
        }
    }

    @Autowired
    private ProductService_PlaceOrder productService;
    @Autowired
    private OrderService_PlaceOrder orderService;
    @Autowired
    private EmailNotification_PlaceOrder nonDBService;
    @Autowired
    private DeliveryInformationService deliveryInformationService;
    @Autowired
    private OrderlineService_PlaceOrder orderlineService;


    public boolean validateDeliveryInfoStringLength(DeliveryInformation deliveryInfo) {
        if (deliveryInfo == null) {
            return false;
        }

        // Check each string field
        if (deliveryInfo.getName() != null && deliveryInfo.getName().length() > 100) {
            return false;
        }

        if (deliveryInfo.getPhone() != null && deliveryInfo.getPhone().length() > 100) {
            return false;
        }

        if (deliveryInfo.getEmail() != null && deliveryInfo.getEmail().length() > 100) {
            return false;
        }

        if (deliveryInfo.getAddress() != null && deliveryInfo.getAddress().length() > 100) {
            return false;
        }

        if (deliveryInfo.getProvince() != null && deliveryInfo.getProvince().length() > 100) {
            return false;
        }

        if (deliveryInfo.getDelivery_message() != null && deliveryInfo.getDelivery_message().length() > 100) {
            return false;
        }
        return true;
    }

    @PostMapping("/placeorder")
    public ResponseEntity<PlaceOrderResponse> RequestToPlaceOrder(@RequestBody Cart cart){
        if(cart == null){
            return ResponseEntity.badRequest().body(new PlaceOrderResponse(null,"The cart is null"));
        }
        try{
        for(CartItem cartItem : cart.getProducts()){
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            if(product == null){
                throw new PlaceOrderException("Product is null");
            }
            boolean result = productService.checkProductValidity(quantity,product.getProduct_id());
            if(!result){
                System.out.println("Failed");
                return ResponseEntity.badRequest().body(new PlaceOrderResponse(null,"The number of product selected is invalid. Please reselect it"));
            }
        }
        Order order = orderService.createOrder(cart);
            PlaceOrderResponse response = new PlaceOrderResponse(order, "Successfully");
            return ResponseEntity.ok(response);
        } catch (PlaceOrderException e) {
            return ResponseEntity.badRequest().body(new PlaceOrderResponse(null,"The server has error. Please try again later."));
        }
    }


    @PostMapping("/deliveryinfo")
    public ResponseEntity<DeliveryInfoResponse> SubmitDeliveryInformation(@RequestBody DeliveryInformation deliveryInfo){
        try {
            // Validate string field lengths before processing
            if(!validateDeliveryInfoStringLength(deliveryInfo)){
                return ResponseEntity.badRequest().body(new DeliveryInfoResponse(null,"The delivery information is invalid. Please reselect it"));
            }
            
            DeliveryInformation deliveryInformation = deliveryInformationService.createDeliveryInfo(deliveryInfo.getName(),deliveryInfo.getPhone(),deliveryInfo.getEmail(),deliveryInfo.getAddress(),deliveryInfo.getProvince(),deliveryInfo.getDelivery_message(),deliveryInfo.getDelivery_fee());
            DeliveryInfoResponse response = new DeliveryInfoResponse(deliveryInformation,null);
            return ResponseEntity.ok(response);
        } catch (PlaceOrderException e) {
            // Return error response when validation fails
            return ResponseEntity.badRequest().body(new DeliveryInfoResponse(null,"The server has error. Please try again later."));
        }
    }

    @PostMapping("/recalculate")
    public ResponseEntity<RecalculateResponse> recalculate(@RequestBody CalculateFeeDTO feeInfoDTO) {
        try {
           IDeliveryFeeCalculating service = DeliveryFeeCalculatingsMap.get(calculating_service_version);
           int[] deliveryfees = service.CalculateDeliveryFee(feeInfoDTO);
           int deliveryfee = deliveryfees[0] + deliveryfees[1];
           RecalculateResponse response = new RecalculateResponse(deliveryfees[0], deliveryfees[1], deliveryfee,"sucessfully");
           return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RecalculateResponse(0,0,0,"The server has error. Please try again later."));
        }
    }

    @PostMapping("/finish-order")
    public ResponseEntity<FinishOrderResponse> FinishPlaceOrder(@RequestBody Order_DeliveryInfo orderInfoDTO){
        try{
        DeliveryInformation deliveryInformation = orderInfoDTO.getDeliveryInformation();
        Order order = orderInfoDTO.getOrder();
        int delivery_id = deliveryInformationService.saveDeliveryInfo(deliveryInformation);
        order.setDelivery_id(delivery_id);
        orderService.saveOrder(order);
        orderlineService.saveOrderlines(order);
        // Cập nhật số lượng sản phẩm sau khi lưu orderline
        nonDBService.SendSuccessEmail(deliveryInformation.getEmail(),"Thông báo về việc đặt hàng","Bạn đã đặt hàng thành công. Mã đơn hàng của bạn là:" + order.getOrder_id());
        FinishOrderResponse response = new FinishOrderResponse(1,"sucessfully");
        return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FinishOrderResponse(0,"The server has error. Please try again later."));
        }
    }
}


