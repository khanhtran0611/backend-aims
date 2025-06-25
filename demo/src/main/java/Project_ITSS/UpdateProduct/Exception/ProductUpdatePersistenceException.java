package Project_ITSS.UpdateProduct.Exception;

public class ProductUpdatePersistenceException extends UpdateProductException {
    public ProductUpdatePersistenceException(String message) {
        super(message, "PERSISTENCE_ERROR");
    }
    
    public ProductUpdatePersistenceException(String message, Throwable cause) {
        super(message, "PERSISTENCE_ERROR", cause);
    }
} 