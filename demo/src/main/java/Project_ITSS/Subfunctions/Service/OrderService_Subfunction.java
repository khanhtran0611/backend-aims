package Project_ITSS.Subfunctions.Service;
//package Project_ITSS.Subfunctions.DTO;
import Project_ITSS.Subfunctions.DTO.OrderInfo;
import Project_ITSS.Subfunctions.Repository.OrderRepository_Subfunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService_Subfunction {
    @Autowired
    private OrderRepository_Subfunction orderRepository;


    public List<OrderInfo> getAllOrders(){
         return orderRepository.getALlOrders();
    }




    // ... các method khác cho order thường
} 