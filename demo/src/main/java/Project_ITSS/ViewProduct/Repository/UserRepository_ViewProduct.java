package Project_ITSS.ViewProduct.Repository;

import Project_ITSS.ViewProduct.Exception.ViewProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository_ViewProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String verifyUserRole(int user_id) {
        try{
            String sql = "SELECT role FROM User WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{user_id}, String.class);
        } catch (Exception e) {
            throw new ViewProductException(e.getMessage());
        }
    }
}
