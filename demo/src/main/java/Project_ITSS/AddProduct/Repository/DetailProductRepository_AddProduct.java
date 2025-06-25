package Project_ITSS.AddProduct.Repository;

import Project_ITSS.AddProduct.Entity.Product;

public interface DetailProductRepository_AddProduct {
    void insertProductInfo(Product product);
    String getType();
}
