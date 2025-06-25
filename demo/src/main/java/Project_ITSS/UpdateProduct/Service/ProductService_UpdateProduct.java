package Project_ITSS.UpdateProduct.Service;

import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Exception.UpdateProductException;
import Project_ITSS.UpdateProduct.Exception.ProductUpdatePersistenceException;
import Project_ITSS.UpdateProduct.Repository.DetailProductRepository_UpdateProduct;
import Project_ITSS.UpdateProduct.Repository.ProductRepository_UpdateProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService_UpdateProduct {
    @Autowired
    private ProductRepository_UpdateProduct productRepository;
    
    @Autowired
    private ProductUpdateValidationService validationService;

    private final Map<String, DetailProductRepository_UpdateProduct> repositoryMap;
    
    @Autowired
    public ProductService_UpdateProduct(List<DetailProductRepository_UpdateProduct> repositories) {
        repositoryMap = new HashMap<>();
        for (DetailProductRepository_UpdateProduct repo : repositories) {
            repositoryMap.put(repo.getType(), repo);
        }
    }

    @Transactional
    public int updateProduct(Product product) {
        try {
            // Validate product before processing
            validationService.validateProductForUpdate(product);
            
            // Update basic product information
            updateProductInfo(product);
            
            // Update type-specific details
            updateProductDetail(product, product.getType());
            
            return (int) product.getProduct_id();
        } catch (ProductUpdatePersistenceException e) {
            throw new UpdateProductException("Database operation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UpdateProductException("Unexpected error occurred while updating product", e);
        }
    }

    private void updateProductInfo(Product product) {
        productRepository.updateProductInfo(product);
    }

    private void updateProductDetail(Product product, String type) {
        DetailProductRepository_UpdateProduct repo = repositoryMap.get(type);
        if (repo == null) {
            throw new UpdateProductException("Unsupported product type: " + type);
        }
        
        try {
            repo.updateProductInfo(product);
        } catch (Exception e) {
            throw new ProductUpdatePersistenceException("Failed to update product detail for type: " + type, e);
        }
    }
}
