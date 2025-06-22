package Project_ITSS.AddProduct.Repository;


import Project_ITSS.AddProduct.Entity.CD;
import Project_ITSS.AddProduct.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class CDRepository_AddProduct implements  DetailProductRepository_AddProduct{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertProductInfo(Product product){
         CD cd = (CD)product;
        String importDateStr = cd.getRelease_date(); // ví dụ "2023-05-30"
        LocalDate localDate = LocalDate.parse(importDateStr);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
        try{
            jdbcTemplate.update(
                    "INSERT INTO CD (" +
                            "    Product_id," +
                            "    Track_list," +
                            "    genre," +
                            "    record_label," +
                            "    artists," +
                            "    release_date" +
                            ") VALUES (" +
                            "    ?, ?, ?, ?, ?, ?" + ")",
                    cd.getProduct_id(),
                    cd.getTrack_list(),
                    cd.getGenre(),
                    cd.getRecord_label(),
                    cd.getArtists(),
                    sqlDate);
        }catch(Exception e){
            System.out.println(e);
        }

    }

    @Override
    public String getType() {
        return "cd";
    }


}
