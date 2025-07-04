package controller;

import Project_ITSS.UpdateProduct.Controller.UpdateProductController;
import Project_ITSS.UpdateProduct.Entity.Product;
import Project_ITSS.UpdateProduct.Service.LoggerService_UpdateProduct;
import Project_ITSS.UpdateProduct.Service.ProductService_UpdateProduct;
import Project_ITSS.Project_ITSSApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UpdateProductController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
class UpdateProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService_UpdateProduct productService;

    @MockBean
    private LoggerService_UpdateProduct loggerService;

    @Test
    void submitProductInfo_khiHopLe_nenTraVeSuccess() throws Exception {
        // Arrange
        Product productToUpdate = new Product();
        productToUpdate.setProduct_id(1L);
        productToUpdate.setTitle("Updated Book");
        productToUpdate.setPrice(15); // int
        productToUpdate.setImage_url("image.jpg");
        productToUpdate.setType("Book");
        productToUpdate.setQuantity(99);
        productToUpdate.setIntroduction("description");
        int expectedProductId = 1;

        when(productService.updateProduct(any(Product.class))).thenReturn(expectedProductId);
        doNothing().when(loggerService).saveLogger(any(Product.class));

        // Act & Assert
        mockMvc.perform(post("/updating/ProductInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.productId").value(expectedProductId));

        verify(productService, times(1)).updateProduct(any(Product.class));
        verify(loggerService, times(1)).saveLogger(any(Product.class));
    }
}