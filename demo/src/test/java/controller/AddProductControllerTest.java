package controller;

import Project_ITSS.AddProduct.Controller.AddProductController;
import Project_ITSS.AddProduct.Entity.Book;
import Project_ITSS.AddProduct.Entity.Product;
import Project_ITSS.AddProduct.Service.LoggerService_AddProduct;
import Project_ITSS.AddProduct.Service.ProductService_Addproduct;
import Project_ITSS.Project_ITSSApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the AddProductController.
 * This class uses @WebMvcTest to test the controller layer without starting a full HTTP server.
 */
@WebMvcTest(AddProductController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
class AddProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService_Addproduct productService;

    @MockBean
    private LoggerService_AddProduct loggerService;

    /**
     * Test case for the /AddingRequested endpoint.
     * It verifies that the endpoint returns an HTTP 200 OK status and the expected string message.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void requestToAddProduct_shouldReturnOkMessage() throws Exception {
        mockMvc.perform(get("/AddingRequested"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product addition request received"));
    }

    /**
     * Test case for a successful product submission via the /adding/ProductInfo endpoint.
     * It mocks the service layer to simulate a successful product addition and verifies that:
     * 1. The controller returns an HTTP 200 OK status.
     * 2. The response body matches the structure of the ProductResponse DTO for a success case.
     * 3. The product service and logger service methods are called exactly once.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void submitProductInfo_whenValidProduct_shouldReturnSuccessResponse() throws Exception {
        // Arrange: Create a sample product to be sent in the request body.
        Book newProduct = new Book();
        newProduct.setTitle("Test Book");
        newProduct.setPrice(100);
        newProduct.setWeight(0.5f);
        newProduct.setQuantity(10);
        newProduct.setImport_date("2025-06-26");
        newProduct.setType("book");
        // Add other necessary fields for a valid book...

        int expectedProductId = 123;

        // Mock the behavior of the product service to return the expected product ID.
        when(productService.addProduct(any(Product.class))).thenReturn(expectedProductId);
        // Mock the logger service to do nothing when called.
        doNothing().when(loggerService).saveLogger(any(Product.class));

        // Act & Assert: Perform a POST request and verify the response.
        mockMvc.perform(post("/adding/ProductInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.message").value("Product added successfully"))
                .andExpect(jsonPath("$.product_id").value(expectedProductId)); // Corrected from productId to product_id

        // Verify that the mocked services were called as expected.
        verify(productService, times(1)).addProduct(any(Product.class));
        verify(loggerService, times(1)).saveLogger(any(Product.class));
    }

    /**
     * Test case for the /adding/available endpoint when adding is allowed.
     * It mocks the logger service to return true and verifies that the endpoint
     * responds with an HTTP 200 OK status and a body of "true".
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void checkValidAdding_whenAllowed_shouldReturnTrue() throws Exception {
        // Arrange: Mock the logger service to indicate that adding is valid.
        when(loggerService.checkValidAddProducts()).thenReturn(true);

        // Act & Assert: Perform a GET request and verify the response.
        mockMvc.perform(get("/adding/available"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Test case for the /adding/available endpoint when adding is not allowed.
     * It mocks the logger service to return false and verifies that the endpoint
     * responds with an HTTP 200 OK status and a body of "false".
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void checkValidAdding_whenNotAllowed_shouldReturnFalse() throws Exception {
        // Arrange: Mock the logger service to indicate that adding is not valid.
        when(loggerService.checkValidAddProducts()).thenReturn(false);

        // Act & Assert: Perform a GET request and verify the response.
        mockMvc.perform(get("/adding/available"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
