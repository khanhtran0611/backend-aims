package Project_ITSS.UpdateProduct.Controller;

import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Service.LoggerService_UpdateProduct;
import Project_ITSS.UpdateProduct.Service.ProductService_UpdateProduct;
import Project_ITSS.UpdateProduct.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UpdateProductController {

    @Autowired
    private ProductService_UpdateProduct productService;
    
    @Autowired
    private LoggerService_UpdateProduct loggerService;

    @GetMapping("/updating/available")
    public ResponseEntity<Boolean> checkValidUpdating() {
        return ResponseEntity.ok(loggerService.checkValidUpdateProducts());
    }

    @GetMapping("/UpdatingRequested")
    public ResponseEntity<String> requestToUpdateProduct() {
        return ResponseEntity.ok("Product update request received");
    }

    @PostMapping("/updating/ProductInfo")
    public ResponseEntity<ProductResponse> updateProductInfo(@RequestBody Product product) {
        // Delegate business logic to service layer
        int productId = productService.updateProduct(product);
        
        // Log the successful operation
        loggerService.saveLogger(product);
        
        return ResponseEntity.ok(ProductResponse.success(productId));
    }
}
