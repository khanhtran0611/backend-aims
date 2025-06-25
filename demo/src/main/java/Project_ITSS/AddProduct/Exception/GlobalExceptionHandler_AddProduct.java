package Project_ITSS.AddProduct.Exception;

import Project_ITSS.AddProduct.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler_AddProduct {

    @ExceptionHandler(AddProductException.class)
    public ResponseEntity<ProductResponse> handleAddProductException(AddProductException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ProductResponse.error(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<ProductResponse> handleValidationException(ProductValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ProductResponse.error(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(ProductPersistenceException.class)
    public ResponseEntity<ProductResponse> handlePersistenceException(ProductPersistenceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ProductResponse.error("Database operation failed", "PERSISTENCE_ERROR"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProductResponse> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ProductResponse.error("An unexpected error occurred", "INTERNAL_ERROR"));
    }
} 