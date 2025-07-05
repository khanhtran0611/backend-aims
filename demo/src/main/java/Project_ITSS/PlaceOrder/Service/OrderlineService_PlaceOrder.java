package Project_ITSS.PlaceOrder.Service;


import Project_ITSS.PlaceOrder.Entity.Order;
import Project_ITSS.PlaceOrder.Entity.Orderline;
import Project_ITSS.PlaceOrder.Repository.OrderlineRepository_PlaceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderlineService_PlaceOrder {
    @Autowired
    private OrderlineRepository_PlaceOrder orderlineRepository;

    public void saveOrderlines(Order order){
        for(Orderline orderline : order.getOrderLineList()){
            orderlineRepository.saveOrderline(orderline,order.getOrder_id());
        }
    }

    public Orderline createOrderline(int product_id,int quantity,int price){
        Orderline orderline = new Orderline();
        orderline.setTotal_fee(quantity * price);
        orderline.setQuantity(quantity);
        orderline.setStatus("pending");
        orderline.setProduct_id(product_id);
        return orderline;
    }


}
