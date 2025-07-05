package Project_ITSS.Subfunctions.Repository;

import Project_ITSS.Subfunctions.Entity.Orderline;
import Project_ITSS.Subfunctions.DTO.ProductItem;
import Project_ITSS.Subfunctions.Exception.PlaceOrderException;
import Project_ITSS.Subfunctions.Repository.mapper.ProductItemRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderlineRepository_Subfunction {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Orderline getOrderlinebyId(int odrline_id){
        try{
            String sql = "SELECT * FROM OrderLines WHERE odrline_id = ?";
            return jdbcTemplate.queryForObject(sql,new Object[]{odrline_id},new BeanPropertyRowMapper<>(Orderline.class));
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
    }

    public List<Orderline> getOrderLinebyOrderId(int order_id){
        try{
            String sql = "SELECT * FROM OrderLines WHERE order_id = ?";
            return jdbcTemplate.query(sql,new Object[]{order_id},new BeanPropertyRowMapper<>(Orderline.class));
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
    }


    public List<ProductItem> getProductItemByOrderId(int order_id){
        try{
        String sql = "SELECT ol.*,p.title,p.price FROM Product p JOIN Orderline ol USING(product_id) JOIN \"Order\" o USING(order_id) WHERE o.order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{order_id}, new ProductItemRowMapper());
        }catch(Exception e){
            e.printStackTrace();
            throw new PlaceOrderException(e.getMessage());
        }
    }



}
