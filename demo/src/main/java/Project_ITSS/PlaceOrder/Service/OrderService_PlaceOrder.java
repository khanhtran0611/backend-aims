package Project_ITSS.PlaceOrder.Service;

import Project_ITSS.PlaceOrder.Entity.*;
import Project_ITSS.PlaceOrder.Repository.OrderRepository_PlaceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService_PlaceOrder {
    @Autowired
    private OrderRepository_PlaceOrder orderRepository;

    @Autowired
    private OrderlineService_PlaceOrder orderlineService;

    public void saveOrder(Order order){
        orderRepository.saveOrder(order);
    }


    public Order createOrder(Cart cart){
        Order order = new Order();
        int Total_before_VAT = 0;
        int Total_after_VAT = 0;
        for(CartItem Cartproduct  : cart.getProducts()){
            Product product = Cartproduct.getProduct();
            int quantity = Cartproduct.getQuantity();
            Total_before_VAT += quantity * product.getPrice();
            Total_after_VAT += (quantity * product.getPrice()) + ((quantity * product.getPrice()) * order.getVAT())/100;
            Orderline orderline = orderlineService.createOrderline(product.getProduct_id(),quantity,product.getPrice());
            (order.getOrderLineList()).add(orderline);
        }
        order.setStatus("pending");
        order.setTotal_before_VAT(Total_before_VAT);
        order.setTotal_after_VAT(Total_after_VAT);
        return order;
    }


    // ... các method khác cho order thường
} 