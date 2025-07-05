package Project_ITSS.Subfunctions.Controller;


import Project_ITSS.Subfunctions.DTO.OrderDetailResponse;
import Project_ITSS.Subfunctions.DTO.OrderInfo;
import Project_ITSS.Subfunctions.DTO.OrderTrackingInfo;
import Project_ITSS.Subfunctions.Service.OrderService_Subfunction;
import Project_ITSS.Subfunctions.Service.ProcessTrackingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OrderManagementController {
    @Autowired
    private ProcessTrackingInfo processTrackingInfo;
    @Autowired
    private OrderService_Subfunction orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderInfo>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/order-detail")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@RequestParam("order_id") int order_id){
        try{
            OrderTrackingInfo order = processTrackingInfo.getTrackingOrder(order_id);
            OrderDetailResponse response = new OrderDetailResponse(1, "Order details is successfully taken !", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            OrderDetailResponse response = new OrderDetailResponse(0, "Order details is failed to be taken !", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
