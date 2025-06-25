package Project_ITSS.AddProduct.Controller;

import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Service.LoggerService_AddProduct;
import Project_ITSS.AddProduct.Service.ProductService_Addproduct;
import Project_ITSS.AddProduct.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AddProductController {

    @Autowired
    private ProductService_Addproduct productService;
    
    @Autowired
    private LoggerService_AddProduct loggerService;

    @GetMapping("/AddingRequested")
    public ResponseEntity<String> requestToAddProduct() {
        return ResponseEntity.ok("Product addition request received");
    }

    @PostMapping("/adding/ProductInfo")
    public ResponseEntity<ProductResponse> submitProductInfo(@RequestBody Product product) {
        // Delegate business logic to service layer
        int productId = productService.addProduct(product);
        
        // Log the successful operation
        loggerService.saveLogger(product);
        
        return ResponseEntity.ok(ProductResponse.success(productId));
    }

    @GetMapping("/adding/available")
    public ResponseEntity<Boolean> checkValidAdding() {
        return ResponseEntity.ok(loggerService.checkValidAddProducts());
    }
}