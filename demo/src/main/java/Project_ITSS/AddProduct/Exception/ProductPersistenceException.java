package Project_ITSS.AddProduct.Exception;

public class ProductPersistenceException extends AddProductException {
    public ProductPersistenceException(String message) {
        super(message);
    }
    
    public ProductPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
} 