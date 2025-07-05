package Project_ITSS.CancelOrder.Repository;

import Project_ITSS.CancelOrder.Entity.DeliveryInformation;
import Project_ITSS.CancelOrder.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository_CancelOrder {
    @Autowired
    private JdbcTemplate jdbcTemplate;


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
}


