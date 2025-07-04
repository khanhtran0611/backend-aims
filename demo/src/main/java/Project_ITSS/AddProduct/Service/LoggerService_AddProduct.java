package Project_ITSS.AddProduct.Service;

import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Repository.LoggerRepository_AddProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerService_AddProduct {
    @Autowired
    private LoggerRepository_AddProduct loggerRepository;

    public void saveLogger(Product product) {
        try {
            loggerRepository.saveLogger("add product", "added product with id: " + product.getProduct_id());
        } catch (Exception e) {
            // Log the error but don't fail the main operation
            System.err.println("Failed to save logger: " + e.getMessage());
        }
    }
    
    public void saveLogger(String action, String note) {
        try {
            loggerRepository.saveLogger(action, note);
        } catch (Exception e) {
            // Log the error but don't fail the main operation
            System.err.println("Failed to save logger: " + e.getMessage());
        }
    }

    public boolean checkValidAddProducts() {

        return loggerRepository.checkValidAddProducts();
    }
}
