package Project_ITSS.UpdateProduct.Exception;

public class ProductUpdateValidationException extends UpdateProductException {
    public ProductUpdateValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
    
    public ProductUpdateValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", cause);
    }
} 