package Project_ITSS.UpdateProduct.Repository;

import Project_ITSS.UpdateProduct.Entity.Book;
import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Exception.ProductUpdatePersistenceException;
import Project_ITSS.UpdateProduct.config.SqlQueries;
import Project_ITSS.UpdateProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository_UpdateProduct implements DetailProductRepository_UpdateProduct {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateProductInfo(Product product) {
        try {
            Book book = (Book) product;
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(book.getPublication_date());
            
            jdbcTemplate.update(
                SqlQueries.UPDATE_BOOK,
                book.getGenre(),
                book.getPage_count(),
                sqlDate,
                book.getAuthors(),
                book.getPublishers(),
                book.getCover_type(),
                book.getProduct_id()
            );
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to update book information", e);
        }
    }

    @Override
    public String getType() {
        return "book";
    }
}
