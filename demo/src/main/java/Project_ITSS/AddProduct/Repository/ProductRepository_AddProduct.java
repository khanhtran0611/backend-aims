package Project_ITSS.AddProduct.Repository;

import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Exception.ProductPersistenceException;
import Project_ITSS.AddProduct.config.SqlQueries;
import Project_ITSS.AddProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository_AddProduct {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertProductInfo(Product product) {
        try {
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(product.getImport_date());
            
            return jdbcTemplate.queryForObject(
                SqlQueries.INSERT_PRODUCT,
                Integer.class,
                product.getTitle(),
                product.getPrice(),
                product.getWeight(),
                product.isRush_order_supported(),
                product.getImage_url(),
                product.getBarcode(),
                sqlDate,
                product.getIntroduction(),
                product.getQuantity(),
                product.getType()
            );
        } catch (Exception e) {
            throw new ProductPersistenceException("Failed to insert product information", e);
        }
    }

    public void updateProduct(Product product) {
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
            throw new ProductPersistenceException("Failed to update product", e);
        }
    }
}