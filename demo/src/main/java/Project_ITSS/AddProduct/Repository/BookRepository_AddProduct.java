package Project_ITSS.AddProduct.Repository;


import Project_ITSS.AddProduct.Entity.Book;
import Project_ITSS.AddProduct.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class BookRepository_AddProduct implements DetailProductRepository_AddProduct {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertProductInfo(Product product){
         Book book = (Book)product;
        String importDateStr = book.getPublication_date(); // ví dụ "2023-05-30"
        LocalDate localDate = LocalDate.parse(importDateStr);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
        System.out.println(book.getAuthors());
        System.out.println(book.getProduct_id());
        System.out.println(book.getGenre());
        System.out.println(book.getPage_count());
        System.out.println(sqlDate);
        System.out.println(book.getAuthors());
        System.out.println(book.getPublishers());
        System.out.println(book.getCover_type());
        try{
            jdbcTemplate.update("INSERT INTO Book " +
                            "(product_id, genre, page_count, publication_date, authors, publishers, cover_type) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    book.getProduct_id(),
                    book.getGenre(),
                    book.getPage_count(),
                    sqlDate,
                    book.getAuthors(),
                    book.getPublishers(),
                    book.getCover_type());
        }catch(Exception e){
            System.out.println(e);
        }

    }

    @Override
    public String getType() {
        return "book";
    }


}
