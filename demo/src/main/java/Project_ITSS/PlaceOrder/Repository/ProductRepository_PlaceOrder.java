package Project_ITSS.PlaceOrder.Repository;

//import Project_ITSS.demo.Entity.Product;
import Project_ITSS.PlaceOrder.Entity.Product;
import Project_ITSS.PlaceOrder.Exception.PlaceOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository_PlaceOrder {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getProductQuantity(int product_id) {
        try{
            return jdbcTemplate.queryForObject("SELECT quantity FROM Product WHERE product_id = ?", new Object[]{product_id}, Integer.class);
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
    }

    public double getProductWeight(int product_id){
        return jdbcTemplate.queryForObject("SELECT weight FROM Product WHERE product_id = ?",new Object[]{product_id}, Double.class);
    }

}
