package Project_ITSS.Subfunctions.Service;


import Project_ITSS.Subfunctions.DTO.OrderTrackingInfo;
import Project_ITSS.Subfunctions.DTO.ProductItem;
import Project_ITSS.Subfunctions.Entity.Order;
import Project_ITSS.Subfunctions.Repository.DeliveryInfoRepository_Subfunction;
import Project_ITSS.Subfunctions.Repository.OrderRepository_Subfunction;
import Project_ITSS.Subfunctions.Repository.OrderlineRepository_Subfunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessTrackingInfo {
    @Autowired
    private OrderRepository_Subfunction orderRepository;
    @Autowired
    private OrderlineRepository_Subfunction orderlineRepository;
    @Autowired
    private DeliveryInfoRepository_Subfunction deliveryInfoRepository;

    public OrderTrackingInfo getTrackingOrder(int order_id){
        Order order = orderRepository.getOrderById(order_id);
        OrderTrackingInfo orderTrackingInfo = new OrderTrackingInfo();
        orderTrackingInfo.setOrder_id(order.getOrder_id());
        orderTrackingInfo.setCurrent_status(order.getStatus());
        orderTrackingInfo.setOrder_date(order.getOrder_time());
        Map<String,Object> json = new HashMap<>();
        json.put("total_amount",order.getTotal_after_VAT());
        String address = deliveryInfoRepository.getCustomerAddress(order_id);
        json.put("payment_method",order.getPayment_method());
        json.put("delivery_address",address);
        List<ProductItem> orderlines = orderlineRepository.getProductItemByOrderId(order_id);
        json.put("items",orderlines);
        orderTrackingInfo.setOrder_details(json);
        return orderTrackingInfo;
    }
}
