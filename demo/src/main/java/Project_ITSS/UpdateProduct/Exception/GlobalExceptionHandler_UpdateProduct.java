package Project_ITSS.UpdateProduct.Exception;

import Project_ITSS.UpdateProduct.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler_UpdateProduct {

    @ExceptionHandler(UpdateProductException.class)
    public ResponseEntity<ProductResponse> handleUpdateProductException(UpdateProductException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ProductResponse.error(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(ProductUpdateValidationException.class)
    public ResponseEntity<ProductResponse> handleValidationException(ProductUpdateValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ProductResponse.error(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(ProductUpdatePersistenceException.class)
    public ResponseEntity<ProductResponse> handlePersistenceException(ProductUpdatePersistenceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ProductResponse.error("Database operation failed", "PERSISTENCE_ERROR"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProductResponse> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ProductResponse.error("An unexpected error occurred", "INTERNAL_ERROR"));
    }
} 