package Project_ITSS.PlaceOrder.Controller;

import Project_ITSS.PlaceOrder.Entity.*;
import Project_ITSS.PlaceOrder.Exception.PlaceOrderException;
import Project_ITSS.PlaceOrder.Repository.OrderRepository_PlaceOrder;
import Project_ITSS.PlaceOrder.Repository.ProductRepository_PlaceOrder;
import Project_ITSS.PlaceOrder.Service.NonDBService_PlaceOrder;
import Project_ITSS.PlaceOrder.Service.OrderService_PlaceOrder;
import Project_ITSS.PlaceOrder.Service.ProductService_PlaceOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
class OrderInfoDTO{
    private Order order;
    private DeliveryInformation deliveryInformation;
}


@RequestMapping("/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@NoArgsConstructor
public class PlaceOrderController {
    @Autowired
    private ProductService_PlaceOrder productService;
    @Autowired
    private OrderService_PlaceOrder orderService;
    @Autowired
    private NonDBService_PlaceOrder nonDBService;
    @Autowired
    private OrderRepository_PlaceOrder orderRepository;

    @GetMapping("/test")
    public void JustForFun(){
        System.out.println(100);
        return;
    }

    @GetMapping("/orders")
    public List<OrderInfo> getALlOrders(){
         return orderService.getAllOrders();
    }


    @PostMapping("/api/order/approve")
    public Map<String, Object> approveOrder(@RequestParam("order_id") long orderId) {
        Map<String, Object> json = new HashMap<>();
        try {
            Order order = orderRepository.getOrderById(orderId);
            if (order == null) {
                json.put("status",0);
                json.put("message","Order not found");
                return json;
            }
            if (!"pending".equalsIgnoreCase(order.getStatus())) {
                json.put("status",0);
                json.put("message","Order status must be 'pending' to approve. Current status: " + order.getStatus());
                return json;
            }
            orderRepository.updateOrderStatusToApprove(orderId);
            json.put("status",1);
            json.put("message","Order " + orderId + " has been approved.");
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json.put("status",0);
            json.put("message","Error approving order: " + e.getMessage());
            return json;
        }
    }

    @PostMapping("/placeorder")
    // Yêu cầu việc đặt hàng và kiểm tra số lượng sản phẩm trong cart liệu có phù hợp
    public Map<String, Object> RequestToPlaceOrder(@RequestBody Cart cart){
        // Kiểm tra số lựong product trong cart có đủ để bán không
        Map<String, Object> json = new HashMap<>();
        if(cart == null){
            throw new PlaceOrderException("The cart is null");
        }
        for(CartItem cartItem : cart.getProducts()){
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            System.out.println(product.getProduct_id());
            if(product == null){
                throw new PlaceOrderException("Product is null");
            }
            boolean result = productService.checkProductValidity(quantity,product.getProduct_id());
            if(!result){
                System.out.println("Falied");
                json.put("message","Inadequate");
                return json;
            }
        }
        Order order = new Order();    // Tạo một entity là order
        order.createOrder(cart);   
        json.put("order",order);
        json.put("message","Successfully");   // Điền thông tin các thuộc tính cho order, dựa trên thông tin của cart
        return json;                 // trả về entity order
    }

    @GetMapping("/order-detail")
    public Map<String,Object> getOrderDetail(@RequestParam("order_id") int order_id){
        Map<String,Object> json = new HashMap<>();
        try{
            OrderTrackingInfo order = orderService.getTrackingInfo(order_id);
            json.put("status",1);
            json.put("message","Order details is successfully taken !");
            json.put("order",order);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json.put("status",0);
            json.put("message","Order details is failed to be taken !");
            return json;
        }


    }


    @PostMapping("/recalculate")
    public Map<String, Object> recalculate(@RequestBody FeeInfoDTO feeInfoDTO) {
        // Phí = 50k + 10k mỗi kg
        String province = feeInfoDTO.getProvince();
        Order order = feeInfoDTO.getOrder();
        System.out.println(province);
        System.out.println(order.getTotal_after_VAT());
        int[] deliveryfees = orderService.CalculateDeliveryFee(province,order);
        int deliveryfee = deliveryfees[0] + deliveryfees[1];
        Map<String, Object> json = new HashMap<>();
        json.put("regularShipping",deliveryfees[0]);
        json.put("rushShipping",deliveryfees[1]);
        json.put("totalShipping",deliveryfee);
        return json;
    }

    // Sau khi người dùng điền thông tin cá nhân, các thông tin sẽ được đưa vào trong hàm này để xử lý tiếp
    @PostMapping("/deliveryinfo")
    public Map<String, Object> SubmitDeliveryInformation(@RequestBody DeliveryInformation deliveryInfo){

        Map<String, Object> json = new HashMap<>();
        DeliveryInformation deliveryInformation = new DeliveryInformation();                        // Tạo entity deliveryInfo
        deliveryInformation.createDeliveryInfo(deliveryInfo.getName(),deliveryInfo.getPhone(),deliveryInfo.getEmail(),deliveryInfo.getAddress(),deliveryInfo.getProvince(),deliveryInfo.getDelivery_message(),deliveryInfo.getDelivery_fee()); // Điền thông tin vào entity đó
//        int[] deliveryfees = orderService.CalculateDeliveryFee(province,order);
//        int deliveryfee = deliveryfees[0] + deliveryfees[1];   // Tính toán giá tiền phải nộp
//        Invoice invoice = new Invoice();                                                            // Tạo entity về Invoice
//        invoice.CreateInvoice(order.getOrder_id(),"Your total delivery fee is " + String.valueOf(deliveryfee) + " and your total amount needed to be paid is " + String.valueOf(order.getTotal_after_VAT()));
        json.put("delivery_information",deliveryInformation);
//        json.put("order",order);
        return json;                                // Trả lại thông tin về invoice lẫn chi phí vận chuyển
    }


    @PostMapping("/finish-order")
    // Sau khi thanh toán xong (Tức sau khi kết thúc PayOrder UseCase) , gọi đến hàm này để xử lý nốt các công đoạn cuối
    public Map<String, Object> FinishPlaceOrder(@RequestBody OrderInfoDTO orderInfoDTO){
        Map<String, Object> json = new HashMap<>();
        DeliveryInformation deliveryInformation = orderInfoDTO.getDeliveryInformation();
        Order order = orderInfoDTO.getOrder();
        System.out.println(deliveryInformation.getDelivery_fee());
        System.out.println(order.getTotal_after_VAT());
        orderService.saveOrder(order,deliveryInformation);      //   Lưu lại thông tin order trong database
        nonDBService.SendSuccessEmail(deliveryInformation.getEmail(),"Thông báo về việc đặt hàng","Bạn đã đật hàng thành công. Mã đơn hàng của bạn là:" + order.getOrder_id());  // Gửi email thông báo
        json.put("status",1);
        return json;
        // khi gửi email , cần gửi một mã order để xử lý refund nếu có.
    }


}


