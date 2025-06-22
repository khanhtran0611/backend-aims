package Project_ITSS.PlaceOrder.Repository;

import Project_ITSS.PlaceOrder.Entity.Orderline;
import Project_ITSS.PlaceOrder.Entity.Product;
import Project_ITSS.PlaceOrder.Entity.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderlineRepository_PlaceOrder {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ProductItem RowMapper
    private static class ProductItemRowMapper implements RowMapper<ProductItem> {
        @Override
        public ProductItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductItem productItem = new ProductItem();
            
            productItem.setProduct_id(rs.getInt("product_id"));
            productItem.setTitle(rs.getString("title"));
            productItem.setPrice(rs.getInt("price"));
            productItem.setQuantity(rs.getInt("quantity"));
            productItem.setRush_order_using(rs.getBoolean("rush_order_using"));
            
            return productItem;
        }
    }

    public Orderline getOrderlinebyId(int odrline_id){
        String sql = "SELECT * FROM OrderLines WHERE odrline_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{odrline_id},new BeanPropertyRowMapper<>(Orderline.class));
    }

    public List<Orderline> getOrderLinebyOrderId(int order_id){
        String sql = "SELECT * FROM OrderLines WHERE order_id = ?";
        return jdbcTemplate.query(sql,new Object[]{order_id},new BeanPropertyRowMapper<>(Orderline.class));
    }

    public void saveOrderline(Orderline orderline,int order_id){
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
    }

    public List<ProductItem> getOrderlineByOrderId(int order_id){
        String sql = "SELECT ol.*,p.title,p.price FROM Product p JOIN Orderline ol USING(product_id) JOIN \"Order\" o USING(order_id) WHERE o.order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{order_id}, new ProductItemRowMapper());
    }


}
