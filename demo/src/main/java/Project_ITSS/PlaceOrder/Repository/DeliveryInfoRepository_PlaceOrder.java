package Project_ITSS.PlaceOrder.Repository;

import Project_ITSS.PlaceOrder.Entity.DeliveryInformation;
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
     }

    public String getCustomerAddress(int order_id){
        String sql = "SELECT address FROM \"Order\" JOIN DeliveryInformation USING(delivery_id) WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{order_id},String.class);
    }

    public DeliveryInformation getDeliveryInformationById(long delivery_id) {
        String sql = "SELECT * FROM DeliveryInformation WHERE delivery_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{delivery_id}, new BeanPropertyRowMapper<>(DeliveryInformation.class));
    }
}
