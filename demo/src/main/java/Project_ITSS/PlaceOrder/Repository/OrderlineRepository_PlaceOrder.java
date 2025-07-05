package Project_ITSS.PlaceOrder.Repository;

import Project_ITSS.PlaceOrder.Entity.Orderline;
import Project_ITSS.PlaceOrder.Exception.PlaceOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderlineRepository_PlaceOrder {
    @Autowired
    private JdbcTemplate jdbcTemplate;



    public void saveOrderline(Orderline orderline,int order_id){
        try{
        String sql = "INSERT INTO Orderline (\n" +
                "    order_id,\n" +
                "    product_id,\n" +
                "    status,\n" +
                "    quantity,\n" +
                "    total_fee,\n" +
                "    rush_order_using,\n" +
                "    delivery_time,\n" +
                "    instructions\n" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?);\n";
        jdbcTemplate.update(sql,
                order_id,
                orderline.getProduct_id(),
                orderline.getStatus(),
                orderline.getQuantity(),
                orderline.getTotal_fee(),
                orderline.isRush_order_using(),
                orderline.getDelivery_time(),
                orderline.getInstructions());
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
    }

}
