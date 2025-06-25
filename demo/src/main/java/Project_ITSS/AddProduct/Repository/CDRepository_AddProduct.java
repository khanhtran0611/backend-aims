package Project_ITSS.AddProduct.Repository;

import Project_ITSS.AddProduct.Entity.CD;
import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Exception.ProductPersistenceException;
import Project_ITSS.AddProduct.config.SqlQueries;
import Project_ITSS.AddProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CDRepository_AddProduct implements DetailProductRepository_AddProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertProductInfo(Product product) {
        try {
            CD cd = (CD) product;
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(cd.getRelease_date());
            
            jdbcTemplate.update(
                SqlQueries.INSERT_CD,
                cd.getProduct_id(),
                cd.getTrack_list(),
                cd.getGenre(),
                cd.getRecord_label(),
                cd.getArtists(),
                sqlDate
            );
        } catch (Exception e) {
            throw new ProductPersistenceException("Failed to insert CD information", e);
        }
    }

    @Override
    public String getType() {
        return "cd";
    }
}
