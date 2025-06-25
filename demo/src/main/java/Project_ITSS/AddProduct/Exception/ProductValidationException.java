package Project_ITSS.AddProduct.Exception;

public class ProductValidationException extends AddProductException {
    public ProductValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
} 