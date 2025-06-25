package Project_ITSS.ViewProduct.Service;

import Project_ITSS.ViewProduct.Repository.DetailProductRepository_ViewProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Project_ITSS.ViewProduct.Entity.Product;
import Project_ITSS.ViewProduct.Repository.ProductRepository_ViewProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProductService_ViewProduct {
    private final Map<String, DetailProductRepository_ViewProduct> repositoryMap;
    @Autowired
    private ProductRepository_ViewProduct productRepository;
    @Autowired
    public ProductService_ViewProduct(List<DetailProductRepository_ViewProduct> repositories){
        repositoryMap = new HashMap<>();
        System.out.println("Found " + repositories.size() + " repositories");
        for(DetailProductRepository_ViewProduct repo : repositories){
            String type = repo.getType();
            System.out.println("Adding repository for type: " + type);
            repositoryMap.put(type, repo);
        }
        System.out.println("Repository map contains: " + repositoryMap.keySet());
    }

    public boolean checkProductValidity(int quantity,int product_id){
        int available_quantity = productRepository.getProductQuantity(product_id);
        if (quantity > available_quantity) return false;
        else return true;
    }

    public Product getBasicProductDetail(int id) {
        return productRepository.findById(id);
    }

    public Product getFullProductDetail(int id,String type) {
        System.out.println("Looking for repository with type: '" + type + "'");
        DetailProductRepository_ViewProduct repo = repositoryMap.get(type);

        return repo.getProductDetailInfo(id);
    }

    public List<Product> getAllProduct(){
//        productRepository = new ProductRepository_ViewProduct();
        return productRepository.getAllProduct();
    }
}
