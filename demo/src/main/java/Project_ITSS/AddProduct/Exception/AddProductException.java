package Project_ITSS.AddProduct.Exception;

public class AddProductException extends RuntimeException {
    private final String errorCode;
    
    public AddProductException(String message) {
        super(message);
        this.errorCode = "ADD_PRODUCT_ERROR";
    }
    
    public AddProductException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public AddProductException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ADD_PRODUCT_ERROR";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
