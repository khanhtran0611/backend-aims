package Project_ITSS.Subfunctions.Repository.mapper;

import Project_ITSS.Subfunctions.DTO.ProductItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductItemRowMapper implements RowMapper<ProductItem> {
    @Override
    public ProductItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductItem productItem = new ProductItem();

        productItem.setProduct_id(rs.getInt("product_id"));
        productItem.setTitle(rs.getString("title"));
        productItem.setPrice(rs.getInt("price"));
        productItem.setQuantity(rs.getInt("quantity"));
        productItem.setRush_order_using(rs.getBoolean("rush_order_using"));

        return productItem;
    }
}