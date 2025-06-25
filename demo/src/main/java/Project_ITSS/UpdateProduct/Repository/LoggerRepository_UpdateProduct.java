package Project_ITSS.UpdateProduct.Repository;

import Project_ITSS.UpdateProduct.Exception.ProductUpdatePersistenceException;
import Project_ITSS.UpdateProduct.config.SqlQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoggerRepository_UpdateProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveLogger(String action_name, String note) {
        try {
            jdbcTemplate.update(SqlQueries.INSERT_LOGGER, action_name, note);
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to save logger", e);
        }
    }

    public int getUpdatingtimes() {
        String sql = "SELECT COUNT(DISTINCT note) FROM Logger WHERE recorded_at = CURRENT_DATE AND action_name = 'update product'";
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
            return result != null ? result : 0;
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to get updating times", e);
        }
    }
}
