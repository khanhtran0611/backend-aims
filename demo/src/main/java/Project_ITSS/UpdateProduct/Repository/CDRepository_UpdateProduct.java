package Project_ITSS.UpdateProduct.Repository;

import Project_ITSS.UpdateProduct.Entity.CD;
import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Exception.ProductUpdatePersistenceException;
import Project_ITSS.UpdateProduct.config.SqlQueries;
import Project_ITSS.UpdateProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CDRepository_UpdateProduct implements DetailProductRepository_UpdateProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateProductInfo(Product product) {
        try {
            CD cd = (CD) product;
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(cd.getRelease_date());
            
            jdbcTemplate.update(
                SqlQueries.UPDATE_CD,
                cd.getTrack_list(),
                cd.getGenre(),
                cd.getRecord_label(),
                cd.getArtists(),
                sqlDate,
                cd.getProduct_id()
            );
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to update CD information", e);
        }
    }

    @Override
    public String getType() {
        return "cd";
    }
}
