package Project_ITSS.CancelOrder.Repository;

import Project_ITSS.CancelOrder.Entity.Orderline;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderlineRepository_CancelOrder {
    private JdbcTemplate jdbcTemplate;

    public Orderline getOrderlinebyId(int odrline_id){
        String sql = "SELECT * FROM OrderLines WHERE odrline_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{odrline_id},new BeanPropertyRowMapper<>(Orderline.class));
    }

    public List<Orderline> getOrderLinebyOrderId(int order_id){
        String sql = "SELECT * FROM OrderLines WHERE order_id = ?";
        return jdbcTemplate.query(sql,new Object[]{order_id},new BeanPropertyRowMapper<>(Orderline.class));
    }

}
