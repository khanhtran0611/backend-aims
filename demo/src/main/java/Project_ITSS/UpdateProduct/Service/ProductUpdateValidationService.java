package Project_ITSS.UpdateProduct.Service;

import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Exception.ProductUpdateValidationException;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateValidationService {
    
    public void validateProductForUpdate(Product product) {
        if (product == null) {
            throw new ProductUpdateValidationException("Product cannot be null");
        }
        
        if (product.getProduct_id() <= 0) {
            throw new ProductUpdateValidationException("Product ID is required for update");
        }
        
        if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            throw new ProductUpdateValidationException("Product title is required");
        }
        
        if (product.getPrice() <= 0) {
            throw new ProductUpdateValidationException("Product price must be greater than 0");
        }
        
        if (product.getWeight() <= 0) {
            throw new ProductUpdateValidationException("Product weight must be greater than 0");
        }
        
        if (product.getQuantity() < 0) {
            throw new ProductUpdateValidationException("Product quantity cannot be negative");
        }
        
        if (product.getType() == null || product.getType().trim().isEmpty()) {
            throw new ProductUpdateValidationException("Product type is required");
        }
        
        validateProductTypeForUpdate(product);
    }
    
    private void validateProductTypeForUpdate(Product product) {
        String type = product.getType().toLowerCase();
        switch (type) {
            case "book":
                validateBookForUpdate(product);
                break;
            case "cd":
                validateCDForUpdate(product);
                break;
            case "dvd":
                validateDVDForUpdate(product);
                break;
            default:
                throw new ProductUpdateValidationException("Invalid product type: " + type);
        }
    }
    
    private void validateBookForUpdate(Product product) {
        if (!(product instanceof Project_ITSS.UpdateProduct.Entity.Book)) {
            throw new ProductUpdateValidationException("Product type 'book' must be a Book instance");
        }
        
        Project_ITSS.UpdateProduct.Entity.Book book = (Project_ITSS.UpdateProduct.Entity.Book) product;
        if (book.getBook_id() <= 0) {
            throw new ProductUpdateValidationException("Book ID is required for update");
        }
    }
    
    private void validateCDForUpdate(Product product) {
        if (!(product instanceof Project_ITSS.UpdateProduct.Entity.CD)) {
            throw new ProductUpdateValidationException("Product type 'cd' must be a CD instance");
        }
        
        Project_ITSS.UpdateProduct.Entity.CD cd = (Project_ITSS.UpdateProduct.Entity.CD) product;
        if (cd.getCD_id() <= 0) {
            throw new ProductUpdateValidationException("CD ID is required for update");
        }
    }
    
    private void validateDVDForUpdate(Product product) {
        if (!(product instanceof Project_ITSS.UpdateProduct.Entity.DVD)) {
            throw new ProductUpdateValidationException("Product type 'dvd' must be a DVD instance");
        }
        
        Project_ITSS.UpdateProduct.Entity.DVD dvd = (Project_ITSS.UpdateProduct.Entity.DVD) product;
        if (dvd.getDVD_id() <= 0) {
            throw new ProductUpdateValidationException("DVD ID is required for update");
        }
    }
} 