package Project_ITSS.CancelOrder.Entity;


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
    private Long delivery_id;
    private final int VAT = 10;
    List<Orderline> orderlineList = new ArrayList<>();


}
