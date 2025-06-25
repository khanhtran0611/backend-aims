package Project_ITSS.AddProduct.Service;

import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Exception.AddProductException;
import Project_ITSS.AddProduct.Exception.ProductPersistenceException;
import Project_ITSS.AddProduct.Repository.DetailProductRepository_AddProduct;
import Project_ITSS.AddProduct.Repository.ProductRepository_AddProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService_Addproduct {
    @Autowired
    private ProductRepository_AddProduct productRepository;
    
    @Autowired
    private ProductValidationService validationService;

    private final Map<String, DetailProductRepository_AddProduct> repositoryMap;
    
    @Autowired
    public ProductService_Addproduct(List<DetailProductRepository_AddProduct> repositories) {
        repositoryMap = new HashMap<>();
        for (DetailProductRepository_AddProduct repo : repositories) {
            repositoryMap.put(repo.getType(), repo);
        }
    }

    @Transactional
    public int addProduct(Product product) {
        try {
            // Validate product before processing
            validationService.validateProduct(product);
            
            // Insert basic product information
            int productId = insertProductInfo(product);
            if (productId <= 0) {
                throw new AddProductException("Failed to insert product information");
            }
            
            // Set the product ID and insert type-specific details
            product.setProduct_id(productId);
            insertProductDetail(product, product.getType());
            
            return productId;
        } catch (ProductPersistenceException e) {
            throw new AddProductException("Database operation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new AddProductException("Unexpected error occurred while adding product", e);
        }
    }

    private int insertProductInfo(Product product) {
        return productRepository.insertProductInfo(product);
    }

    private void insertProductDetail(Product product, String type) {
        DetailProductRepository_AddProduct repo = repositoryMap.get(type);
        if (repo == null) {
            throw new AddProductException("Unsupported product type: " + type);
        }
        
        try {
            repo.insertProductInfo(product);
        } catch (Exception e) {
            throw new ProductPersistenceException("Failed to insert product detail for type: " + type, e);
        }
    }
} 