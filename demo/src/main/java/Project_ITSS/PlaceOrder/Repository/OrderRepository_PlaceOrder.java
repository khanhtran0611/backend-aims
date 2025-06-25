package Project_ITSS.PlaceOrder.Repository;

import Project_ITSS.PlaceOrder.Entity.DeliveryInformation;
import Project_ITSS.PlaceOrder.Entity.Order;
import Project_ITSS.PlaceOrder.DTO.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository_PlaceOrder {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveOrder(Order order){
        // Insert vào Order, có delivery_id
        jdbcTemplate.update("INSERT INTO \"Order\" (order_id, delivery_id, Total_before_VAT, Total_after_VAT, status, VAT, order_time, payment_method) VALUES (?,?,?,?,?,?,CURRENT_DATE,\'credit_card\')",
            order.getOrder_id(),
            order.getDelivery_id(),
            order.getTotal_before_VAT(),
            order.getTotal_after_VAT(),
            order.getStatus(),
            order.getVAT());

    }

    public Order getOrderById(long order_id){
        String sql = "SELECT * FROM \"Order\" WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{order_id},new BeanPropertyRowMapper<>(Order.class));
    }


    public void updateOrderStatus(long order_id, String status) {
        String sql = "UPDATE \"Order\" SET status = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, status, order_id);
    }

    public DeliveryInformation getDeliveryInformationById(long delivery_id) {
        String sql = "SELECT * FROM DeliveryInformation WHERE delivery_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{delivery_id}, new BeanPropertyRowMapper<>(DeliveryInformation.class));
    }

    public void updateOrderStatusToApprove(long order_id) {
        String sql = "UPDATE \"Order\" SET status = 'approved' WHERE order_id = ?";
        jdbcTemplate.update(sql, order_id);
    }

    public List<OrderInfo> getALlOrders(){
        String sql = "SELECT order_id,name as customer_name, order_time as order_date, total_after_vat as total_amount, status,payment_method from \"Order\" JOIN deliveryinformation using (delivery_id);";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(OrderInfo.class));
    }

    public String getCustomerAddress(int order_id){
        String sql = "SELECT address FROM \"Order\" JOIN DeliveryInformation USING(delivery_id) WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{order_id},String.class);
    }


}


