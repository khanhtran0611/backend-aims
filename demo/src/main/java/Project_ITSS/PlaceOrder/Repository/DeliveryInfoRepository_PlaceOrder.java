package Project_ITSS.PlaceOrder.Repository;

import Project_ITSS.PlaceOrder.Entity.DeliveryInformation;
import Project_ITSS.PlaceOrder.Exception.PlaceOrderException;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryInfoRepository_PlaceOrder {
     @Autowired
     private JdbcTemplate jdbcTemplate;

     public int saveDeliveryInfo(DeliveryInformation dI){
        try{
         String sqlDelivery = "INSERT INTO DeliveryInformation (Name, Phone, Email, Address, Province, Shipping_message, shipping_fee) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING delivery_id";
         return jdbcTemplate.queryForObject(sqlDelivery, new Object[]{
                 dI.getName(),
                 dI.getPhone(),
                 dI.getEmail(),
                 dI.getAddress(),
                 dI.getProvince(),
                 dI.getDelivery_message(),
                 dI.getDelivery_fee()
         }, Integer.class);
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
     }

}
