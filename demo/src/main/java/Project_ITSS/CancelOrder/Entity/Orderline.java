package Project_ITSS.CancelOrder.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Orderline {
    private int odrline_id;
    private int order_id;
    private int product_id;
    private String status;
    private boolean rush_order_using;
    private int quantity;
    private int total_fee;
    private String delivery_time;
    private String instructions;

}
