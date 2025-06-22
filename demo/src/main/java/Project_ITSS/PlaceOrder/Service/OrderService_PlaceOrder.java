package Project_ITSS.PlaceOrder.Service;

import Project_ITSS.PlaceOrder.Entity.*;
import Project_ITSS.PlaceOrder.Repository.OrderRepository_PlaceOrder;
import Project_ITSS.PlaceOrder.Repository.OrderlineRepository_PlaceOrder;
import Project_ITSS.PlaceOrder.Repository.ProductRepository_PlaceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService_PlaceOrder {
    @Autowired
    private OrderRepository_PlaceOrder orderRepository;
    @Autowired
    private ProductRepository_PlaceOrder productRepository;
    @Autowired
    private OrderlineRepository_PlaceOrder orderlineRepository;

    public void saveRushOrder(DeliveryInfo deliveryInfo) {
        // TODO: Lưu thông tin rush order vào DB hoặc xử lý logic liên quan
        System.out.println("Rush order saved: " + deliveryInfo);
    }

    public void saveOrder(Order order, DeliveryInformation deliveryInfo){
        orderRepository.saveOrder(order,deliveryInfo);
        for(Orderline orderline : order.getOrderLineList()){
            orderlineRepository.saveOrderline(orderline,order.getOrder_id());
        }
    }

    public int[] CalculateDeliveryFee(String province,Order order){
        List<Orderline> orderlineList = order.getOrderLineList();
        System.out.println(orderlineList.size());
        int normal_delivery_fee = 0;
        int rush_delivery_fee = 0;
        if(province.equals("HaNoi") || province.equals("HoChiMinhCity")){
            for (Orderline orderline : orderlineList){
                double weight = productRepository.getProductWeight(orderline.getProduct_id());
                int total = 0;
                total += 22000;
                weight = (weight - 3);
                while(weight > 0.5){
                    total += 2500;
                    weight -= 0.5;
                }
                if(orderline.isRush_order_using()){
                    rush_delivery_fee += total + 10000;
                }else{
                    normal_delivery_fee += total;
                }
            }
            normal_delivery_fee = (normal_delivery_fee > 25000) ? normal_delivery_fee - 25000 : 0;
        }else{
            for (Orderline orderline : orderlineList){
                double weight = productRepository.getProductWeight(orderline.getProduct_id());
                System.out.println(weight);
                int total = 0;
                total += 30000;
                weight = (weight - 0.5);
                while(weight > 0.5){
                    total += 2500;
                    weight -= 0.5;
                }
                if(orderline.isRush_order_using()){
                    rush_delivery_fee += total + 10000;
                }else{
                    normal_delivery_fee += total;
                }
            }
        }
        if(order.getTotal_before_VAT() > 100000){
            normal_delivery_fee = (normal_delivery_fee > 25000) ? normal_delivery_fee - 25000 : 0;
        }
        int[] delivery_fees = {normal_delivery_fee,rush_delivery_fee};
        return delivery_fees;
    }

    public List<OrderInfo> getAllOrders(){
         return orderRepository.getALlOrders();
    }



    public OrderTrackingInfo getTrackingInfo(int order_id){
         Order order = orderRepository.getOrderById(order_id);
         OrderTrackingInfo orderTrackingInfo = new OrderTrackingInfo();
         orderTrackingInfo.setOrder_id(order.getOrder_id());
         orderTrackingInfo.setCurrent_status(order.getStatus());
         orderTrackingInfo.setOrder_date(order.getOrder_time());
         Map<String,Object> json = new HashMap<>();
         json.put("total_amount",order.getTotal_after_VAT());
         String address = orderRepository.getCustomerAddress(order_id);
         json.put("payment_method",order.getPayment_method());
         json.put("delivery_address",address);
         List<ProductItem> orderlines = orderlineRepository.getOrderlineByOrderId(order_id);
         json.put("items",orderlines);
         orderTrackingInfo.setOrder_details(json);
         return orderTrackingInfo;
    }

    // ... các method khác cho order thường
} 