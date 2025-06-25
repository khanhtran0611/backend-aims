package Project_ITSS.CancelOrder.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepository_CancelOrder {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
