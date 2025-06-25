package Project_ITSS.AddProduct.Repository;

import Project_ITSS.AddProduct.Entity.DVD;
import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Exception.ProductPersistenceException;
import Project_ITSS.AddProduct.config.SqlQueries;
import Project_ITSS.AddProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DVDRepository_AddProduct implements DetailProductRepository_AddProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertProductInfo(Product product) {
        try {
            DVD dvd = (DVD) product;
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(dvd.getRelease_date());
            
            jdbcTemplate.update(
                SqlQueries.INSERT_DVD,
                dvd.getProduct_id(),
                dvd.getTitle(),
                sqlDate,
                dvd.getDVD_type(),
                dvd.getGenre(),
                dvd.getStudio(),
                dvd.getDirector()
            );
        } catch (Exception e) {
            throw new ProductPersistenceException("Failed to insert DVD information", e);
        }
    }

    @Override
    public String getType() {
        return "dvd";
    }
}
