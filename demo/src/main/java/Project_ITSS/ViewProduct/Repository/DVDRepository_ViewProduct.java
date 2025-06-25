package Project_ITSS.ViewProduct.Repository;

import Project_ITSS.ViewProduct.Entity.Product;
import Project_ITSS.ViewProduct.Exception.ViewProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class DVDRepository_ViewProduct implements DetailProductRepository_ViewProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Product getProductDetailInfo(int product_id) {
        try{
            String sql = "SELECT * FROM DVD JOIN Product USING (product_id) WHERE product_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{product_id}, new DVDRowMapper());
        }catch (Exception e){
            throw new ViewProductException(e.getMessage());
        }
    }

    @Override
    public String getType() {
        return "dvd";
    }
}
