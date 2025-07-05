package Project_ITSS.Subfunctions.Repository;

import Project_ITSS.Subfunctions.Entity.DeliveryInformation;
import Project_ITSS.Subfunctions.Exception.PlaceOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryInfoRepository_Subfunction {
     @Autowired
     private JdbcTemplate jdbcTemplate;



    public String getCustomerAddress(int order_id){
        try{
            String sql = "SELECT address FROM \"Order\" JOIN DeliveryInformation USING(delivery_id) WHERE order_id = ?";
            return jdbcTemplate.queryForObject(sql,new Object[]{order_id},String.class);
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
    }

    public DeliveryInformation getDeliveryInformationById(long delivery_id) {
        try{
            String sql = "SELECT * FROM DeliveryInformation WHERE delivery_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{delivery_id}, new BeanPropertyRowMapper<>(DeliveryInformation.class));
        }catch (Exception e){
            throw new PlaceOrderException(e.getMessage());
        }
    }
}
