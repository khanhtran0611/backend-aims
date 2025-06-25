package Project_ITSS.UpdateProduct.Exception;

public class UpdateProductException extends RuntimeException {
    private final String errorCode;
    
    public UpdateProductException(String message) {
        super(message);
        this.errorCode = "UPDATE_PRODUCT_ERROR";
    }
    
    public UpdateProductException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "UPDATE_PRODUCT_ERROR";
    }
    
    public UpdateProductException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public UpdateProductException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 