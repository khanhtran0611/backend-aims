package Project_ITSS.PlaceOrder.Repository;

import Project_ITSS.PlaceOrder.Entity.Order;
import Project_ITSS.PlaceOrder.Exception.PlaceOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository_PlaceOrder {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveOrder(Order order){
        // Insert vào Order, có delivery_id
        try{
            jdbcTemplate.update("INSERT INTO \"Order\" (order_id, delivery_id, Total_before_VAT, Total_after_VAT, status, VAT, order_time, payment_method) VALUES (?,?,?,?,?,?,CURRENT_DATE,?)",
                    order.getOrder_id(),
                    order.getDelivery_id(),
                    order.getTotal_before_VAT(),
                    order.getTotal_after_VAT(),
                    order.getStatus(),
                    order.getVAT(),
                    order.getPayment_method());
        } catch (Exception e) {
            throw new PlaceOrderException(e.getMessage());
        }
    }
}


