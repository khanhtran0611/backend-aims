package Project_ITSS.AddProduct.Service;

import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Exception.ProductValidationException;
import org.springframework.stereotype.Service;

@Service
public class ProductValidationService {
    
    public void validateProduct(Product product) {
        if (product == null) {
            throw new ProductValidationException("Product cannot be null");
        }
        
        if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            throw new ProductValidationException("Product title is required");
        }
        
        if (product.getPrice() <= 0) {
            throw new ProductValidationException("Product price must be greater than 0");
        }
        
        if (product.getWeight() <= 0) {
            throw new ProductValidationException("Product weight must be greater than 0");
        }
        
        if (product.getQuantity() < 0) {
            throw new ProductValidationException("Product quantity cannot be negative");
        }
        
        if (product.getType() == null || product.getType().trim().isEmpty()) {
            throw new ProductValidationException("Product type is required");
        }
        
        validateProductType(product);
    }
    
    private void validateProductType(Product product) {
        String type = product.getType().toLowerCase();
        switch (type) {
            case "book":
                validateBook(product);
                break;
            case "cd":
                validateCD(product);
                break;
            case "dvd":
                validateDVD(product);
                break;
            default:
                throw new ProductValidationException("Invalid product type: " + type);
        }
    }
    
    private void validateBook(Product product) {
        // Book-specific validation can be added here
        // For now, we'll just ensure it's a Book instance
        if (!(product instanceof Project_ITSS.AddProduct.Entity.Book)) {
            throw new ProductValidationException("Product type 'book' must be a Book instance");
        }
    }
    
    private void validateCD(Product product) {
        if (!(product instanceof Project_ITSS.AddProduct.Entity.CD)) {
            throw new ProductValidationException("Product type 'cd' must be a CD instance");
        }
    }
    
    private void validateDVD(Product product) {
        if (!(product instanceof Project_ITSS.AddProduct.Entity.DVD)) {
            throw new ProductValidationException("Product type 'dvd' must be a DVD instance");
        }
    }
} 