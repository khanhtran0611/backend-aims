package Project_ITSS.Subfunctions.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private int order_id;
    private int Total_before_VAT;
    private int Total_after_VAT;
    private String status;
    private int delivery_id;
    private final int VAT = 10;
    private String order_time;
    private String payment_method;
    List<Orderline> orderlineList = new ArrayList<>();



    // public void createOrder(Cart cart){
    //     for(CartItem Cartproduct  : cart.getProducts()){
    //         Product product = Cartproduct.getProduct();
    //         int quantity = Cartproduct.getQuantity();
    //         this.Total_before_VAT += quantity * product.getPrice();
    //         this.Total_after_VAT += (quantity * product.getPrice()) + ((quantity * product.getPrice()) * this.VAT)/100;
    //         Orderline orderline = new Orderline();
    //         this.setStatus("pending");
    //         orderline.createOrderline(product.getProduct_id(),quantity,product.getPrice());
    //         orderlineList.add(orderline);
    //     }
    // }

    public List<Orderline> getOrderLineList(){
        return orderlineList;
    }





}
