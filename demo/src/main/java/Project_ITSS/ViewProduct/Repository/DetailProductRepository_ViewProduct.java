package Project_ITSS.ViewProduct.Repository;

import Project_ITSS.ViewProduct.Entity.Product;
import Project_ITSS.ViewProduct.Exception.ViewProductException;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailProductRepository_ViewProduct {
    public Product getProductDetailInfo(int product_id);
    public String getType();
}
