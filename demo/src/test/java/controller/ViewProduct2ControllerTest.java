package controller;

import Project_ITSS.ViewProduct.Controller.ViewProductController;
import Project_ITSS.ViewProduct.Entity.Book;
import Project_ITSS.ViewProduct.Entity.Product;
import Project_ITSS.ViewProduct.Exception.ViewProductException;
import Project_ITSS.ViewProduct.Service.ProductService_ViewProduct;
import Project_ITSS.ViewProduct.Service.UserService_ViewProduct;
import Project_ITSS.Project_ITSSApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the ViewProductController.
 * This class uses @WebMvcTest to test the controller layer by mocking its service dependencies.
 */
@WebMvcTest(ViewProductController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
class ViewProduct2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService_ViewProduct productService;

    @MockBean
    private UserService_ViewProduct userService; // Mocked to prevent context load failure.

    /**
     * Tests GET /product/all endpoint.
     * Verifies that it returns a list of all products successfully.
     */
    @Test
    @DisplayName("GET /product/all - Should Return List of Products")
    void getProductAll_whenProductsExist_shouldReturnProductList() throws Exception {
        // Arrange
        Product product = new Product();
        product.setProduct_id(1);
        product.setTitle("A Good Book");
        product.setPrice(150);
        product.setType("book");

        List<Product> allProducts = Collections.singletonList(product);

        when(productService.getAllProduct()).thenReturn(allProducts);

        // Act & Assert
        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("A Good Book")));
    }

    /**
     * Tests GET /product/detail/{id} for fetching basic product details.
     * Verifies a successful response for a valid product ID.
     */
    @Test
    @DisplayName("GET /product/detail/{id} - With Valid ID - Should Return Basic Product Details")
    void getProductDetailForCustomer_whenIdIsValid_shouldReturnProduct() throws Exception {
        // Arrange
        int productId = 1;
        Product product = new Product();
        product.setProduct_id(productId);
        product.setTitle("A Basic Book");
        product.setPrice(120);

        when(productService.getBasicProductDetail(productId)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/product/detail/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_id", is(productId)))
                .andExpect(jsonPath("$.title", is("A Basic Book")));
    }

    /**
     * Tests GET /product/all-detail/{id} for fetching full product details (manager view).
     * Verifies a successful response with type-specific fields for a valid product ID and type.
     */
    @Test
    @DisplayName("GET /product/all-detail/{id} - With Valid ID and Type - Should Return Full Product Details")
    void getProductDetailForManager_whenIdAndTypeAreValid_shouldReturnFullDetails() throws Exception {
        // Arrange
        int productId = 1;
        String productType = "book";

        Book book = new Book();
        book.setProduct_id(productId);
        book.setTitle("A Detailed Book");
        book.setPrice(200);
        book.setType(productType);
        book.setAuthors("John Author");
        book.setPage_count(300);

        when(productService.getFullProductDetail(productId, productType)).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/product/all-detail/{id}", productId)
                        .param("type", productType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_id", is(productId)))
                .andExpect(jsonPath("$.title", is("A Detailed Book")))
                .andExpect(jsonPath("$.authors", is("John Author"))) // Verify book-specific field
                .andExpect(jsonPath("$.page_count", is(300)));      // Verify book-specific field
    }

    /**
     * Tests GET /product/detail/{id} with an invalid ID (zero).
     * The controller is expected to throw a ViewProductException, which we can catch.
     */
    @Test
    @DisplayName("GET /product/detail/{id} - With Invalid ID - Should Throw Exception")
    void getProductDetailForCustomer_whenIdIsInvalid_shouldThrowException() throws Exception {
        // Arrange
        int invalidProductId = 0;

        // Mock the service to throw the expected exception
        when(productService.getBasicProductDetail(invalidProductId))
                .thenThrow(new ViewProductException("The product id is invalid"));

        // Act & Assert
        mockMvc.perform(get("/product/detail/{id}", invalidProductId))
                .andExpect(status().isInternalServerError()); // Default behavior without a specific @ExceptionHandler
    }
}
