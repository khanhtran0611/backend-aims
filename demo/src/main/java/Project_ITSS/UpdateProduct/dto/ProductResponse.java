package Project_ITSS.UpdateProduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int status;
    private String message;
    private Integer productId;
    private String errorCode;
    private String error;
    
    public static ProductResponse success(int productId) {
        return new ProductResponse(1, "Product updated successfully", productId, null, null);
    }
    
    public static ProductResponse error(String error, String errorCode) {
        return new ProductResponse(0, null, null, errorCode, error);
    }
} 