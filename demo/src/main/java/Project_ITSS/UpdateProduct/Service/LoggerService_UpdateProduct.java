package Project_ITSS.UpdateProduct.Service;

import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Repository.LoggerRepository_UpdateProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerService_UpdateProduct {
    @Autowired
    private LoggerRepository_UpdateProduct loggerRepository;

    public void saveLogger(Product product) {
        try {
            loggerRepository.saveLogger("update product", "updated product with id: " + product.getProduct_id());
        } catch (Exception e) {
            // Log error but don't fail the main operation
            System.err.println("Failed to save logger: " + e.getMessage());
        }
    }

    public boolean checkValidUpdateProducts() {
        try {
            int times = loggerRepository.getUpdatingtimes();
            return times <= 30;
        } catch (Exception e) {
            // If logger check fails, allow the operation to proceed
            System.err.println("Failed to check update times: " + e.getMessage());
            return true;
        }
    }
}
