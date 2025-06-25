package Project_ITSS.ViewProduct.Controller;

import Project_ITSS.ViewProduct.Exception.ViewProductException;
import Project_ITSS.ViewProduct.Service.UserService_ViewProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import Project_ITSS.ViewProduct.Entity.Product;
import Project_ITSS.ViewProduct.Service.ProductService_ViewProduct;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/product")
public class ViewProductController {

    @Autowired
    ProductService_ViewProduct productService;

    @Autowired
    UserService_ViewProduct userService;

    @GetMapping("/all-detail/{id}")
    public Product getProductDetailForManager(@PathVariable("id") int id,@RequestParam("type") String type) {
        System.out.println(type);
        if(id <= 0){
            throw new ViewProductException("The product id is invalid");
        }
        return productService.getFullProductDetail(id,type);
    }
    @GetMapping("/detail/{id}")
    public Product getProductDetailForCustomer(@PathVariable("id") int id){
        if(id <= 0){
            throw new ViewProductException("The product id is invalid");
        }
        return productService.getBasicProductDetail(id);
    }

    @GetMapping("/all")
    public List<Product> getProductALl(){
        return productService.getAllProduct();
    }

}
