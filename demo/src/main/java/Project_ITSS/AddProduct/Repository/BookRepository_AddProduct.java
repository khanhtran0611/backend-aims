package Project_ITSS.AddProduct.Repository;

import Project_ITSS.AddProduct.Entity.Book;
import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Exception.ProductPersistenceException;
import Project_ITSS.AddProduct.config.SqlQueries;
import Project_ITSS.AddProduct.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository_AddProduct implements DetailProductRepository_AddProduct {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertProductInfo(Product product) {
        try {
            Book book = (Book) product;
            java.sql.Date sqlDate = DateUtils.parseStringToSqlDate(book.getPublication_date());
            
            jdbcTemplate.update(
                SqlQueries.INSERT_BOOK,
                book.getProduct_id(),
                book.getGenre(),
                book.getPage_count(),
                sqlDate,
                book.getAuthors(),
                book.getPublishers(),
                book.getCover_type()
            );
        } catch (Exception e) {
            throw new ProductPersistenceException("Failed to insert book information", e);
        }
    }

    @Override
    public String getType() {
        return "book";
    }
}
