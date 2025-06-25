package Project_ITSS.AddProduct.Repository;

import Project_ITSS.AddProduct.Exception.ProductPersistenceException;
import Project_ITSS.AddProduct.config.SqlQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoggerRepository_AddProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveLogger(String action_name, String note) {
        try {
            jdbcTemplate.update(SqlQueries.INSERT_LOGGER, action_name, note);
        } catch (Exception e) {
            throw new ProductPersistenceException("Failed to save logger information", e);
        }
    }

    public boolean checkValidAddProducts() {
        String sql = "SELECT COUNT(DISTINCT note) FROM Logger WHERE recorded_at = CURRENT_DATE AND action_name = 'add product'";
        try{
            return jdbcTemplate.queryForObject(sql, Integer.class) <= 30;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

