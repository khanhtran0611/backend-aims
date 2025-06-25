package Project_ITSS.UpdateProduct.Repository;

import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Exception.ProductUpdatePersistenceException;
import Project_ITSS.UpdateProduct.config.SqlQueries;
import Project_ITSS.UpdateProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository_UpdateProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateProductInfo(Product product) {
        try {
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(product.getImport_date());
            
            jdbcTemplate.update(
                SqlQueries.UPDATE_PRODUCT,
                product.getTitle(),
                product.getPrice(),
                product.getWeight(),
                product.isRush_order_supported(),
                product.getImage_url(),
                product.getBarcode(),
                sqlDate,
                product.getIntroduction(),
                product.getQuantity(),
                product.getProduct_id()
            );
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to update product information", e);
        }
    }
}




