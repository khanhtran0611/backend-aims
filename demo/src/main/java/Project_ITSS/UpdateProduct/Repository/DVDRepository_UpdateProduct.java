package Project_ITSS.UpdateProduct.Repository;

import Project_ITSS.UpdateProduct.Entity.DVD;
import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Exception.ProductUpdatePersistenceException;
import Project_ITSS.UpdateProduct.config.SqlQueries;
import Project_ITSS.UpdateProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DVDRepository_UpdateProduct implements DetailProductRepository_UpdateProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateProductInfo(Product product) {
        try {
            DVD dvd = (DVD) product;
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(dvd.getRelease_date());
            
            jdbcTemplate.update(
                SqlQueries.UPDATE_DVD,
                dvd.getTitle(),
                sqlDate,
                dvd.getDVD_type(),
                dvd.getGenre(),
                dvd.getStudio(),
                dvd.getDirector(),
                dvd.getProduct_id()
            );
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to update DVD information", e);
        }
    }

    @Override
    public String getType() {
        return "dvd";
    }
}