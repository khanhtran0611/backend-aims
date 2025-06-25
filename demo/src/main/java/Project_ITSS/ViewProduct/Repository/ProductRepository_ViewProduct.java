package Project_ITSS.ViewProduct.Repository;


import Project_ITSS.ViewProduct.Entity.Product;
import Project_ITSS.ViewProduct.Exception.ViewProductException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public  class ProductRepository_ViewProduct {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getProductQuantity(int product_id){
        try{
            return jdbcTemplate.queryForObject("SELECT quantity FROM Product WHERE product_id = ?", new Object[]{product_id}, Integer.class);
        }catch (Exception e){
            throw new ViewProductException(e.getMessage());
        }
    }

    public int getProductPrice(int product_id){
        try{
            return jdbcTemplate.queryForObject("SELECT price FROM Product WHERE product_id = ?",new Object[]{product_id}, Integer.class);
        }catch (Exception e){
            throw new ViewProductException(e.getMessage());
        }
    }

    public double getProductWeight(int product_id){
        try{
            return jdbcTemplate.queryForObject("SELECT weight FROM Product WHERE product_id = ?",new Object[]{product_id}, Double.class);
        }catch (Exception e){
            throw new ViewProductException(e.getMessage());
        }
    }




    public Product findById(long id) {
        try{
            String sql = "SELECT * FROM product WHERE product_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ProductRowMapper());
        }catch (Exception e){
            throw new ViewProductException(e.getMessage());
        }
    }

    public List<Product> getAllProduct(){
        try {
            String sql = "SELECT * FROM product";
            return jdbcTemplate.query(sql, new ProductRowMapper());
        } catch (Exception e) {
            throw new ViewProductException(e.getMessage());
        }
    }

    



}