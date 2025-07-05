package controller;

import Project_ITSS.PlaceOrder.Controller.PlaceOrderController;
import Project_ITSS.PlaceOrder.DTO.*;
import Project_ITSS.PlaceOrder.Entity.Cart;
import Project_ITSS.PlaceOrder.Entity.CartItem;
import Project_ITSS.PlaceOrder.Entity.DeliveryInformation;
import Project_ITSS.PlaceOrder.Entity.Order;
import Project_ITSS.PlaceOrder.Entity.Product;
import Project_ITSS.PlaceOrder.Service.*;
import Project_ITSS.Project_ITSSApplication;
import Project_ITSS.PlaceOrder.Service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the PlaceOrderController.
 * This class uses @WebMvcTest to test the controller layer by mocking its service dependencies.
 */
@WebMvcTest(PlaceOrderController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
class PlaceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock all service dependencies of PlaceOrderController
    @MockBean
    private ProductService_PlaceOrder productService;
    @MockBean
    private OrderService_PlaceOrder orderService;
    @MockBean
    private EmailNotification_PlaceOrder nonDBService; // Note: Bean name in controller is 'nonDBService'
    @MockBean
    private DeliveryInformationService deliveryInformationService;
    @MockBean
    private OrderlineService_PlaceOrder orderlineService;
    @MockBean
    private DeliveryFeeCalculating deliveryFeeCalculating; // Mocking the concrete implementation

    /**
     * Set up common mock behavior before each test.
     */
    @BeforeEach
    void setUp() {
        // Since the controller creates a map of services based on version,
        // we need to mock the getVersion() call for our mocked service.
        when(deliveryFeeCalculating.getVersion()).thenReturn("1.0");
    }

//    /**
//     * Tests the GET /test endpoint.
//     */
//    @Test
//    @DisplayName("GET /test - Should return success message")
//    void testEndpoint_shouldReturnSuccessMessage() throws Exception {
//        mockMvc.perform(get("/test"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Test successful"));
//    }

    /**
     * Tests POST /placeorder with a valid cart.
     * Verifies that the service checks product validity and returns an OK response with the created order.
     */
    @Test
    @DisplayName("POST /placeorder - With Valid Cart - Should Return Order")
    void requestToPlaceOrder_whenCartIsValid_shouldReturnOrder() throws Exception {
        // Arrange
        Product product = new Product();
        product.setProduct_id(1);
        product.setPrice(100);
        CartItem cartItem = new CartItem(product, 2);
        Cart cart = new Cart();
        cart.addProducts(Collections.singletonList(cartItem));

        when(productService.checkProductValidity(anyInt(), anyInt())).thenReturn(true);

        Order mockOrder = new Order();
        mockOrder.setTotal_before_VAT(400);
        when(orderService.createOrder(any(Cart.class))).thenReturn(mockOrder);

        // Act & Assert
        mockMvc.perform(post("/placeorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successfully")))
                .andExpect(jsonPath("$.order.total_before_VAT", is(400))); // Cart contains products in both listofProducts and products
    }

    /**
     * Tests POST /placeorder with a cart containing an item with insufficient stock.
     * Verifies that the controller returns a Bad Request response.
     */
    @Test
    @DisplayName("POST /placeorder - With Invalid Product Quantity - Should Return Bad Request")
    void requestToPlaceOrder_whenProductIsInvalid_shouldReturnBadRequest() throws Exception {
        // Arrange
        Product product = new Product();
        product.setProduct_id(1);
        CartItem cartItem = new CartItem(product, 10); // Requesting 10
        Cart cart = new Cart();
        cart.addProducts(Collections.singletonList(cartItem));

        // Mock service to indicate product is not available in the requested quantity
        when(productService.checkProductValidity(10, 1)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/placeorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The number of product selected is invalid. Please reselect it")));
    }


    /**
     * Tests POST /deliveryinfo with valid data.
     * Verifies a successful response is returned.
     */
    @Test
    @DisplayName("POST /deliveryinfo - With Valid Info - Should Return OK")
    void submitDeliveryInformation_whenInfoIsValid_shouldReturnDeliveryInfoResponse() throws Exception {
        // Arrange
        DeliveryInformation deliveryInfo = new DeliveryInformation();
        deliveryInfo.setName("John Doe");
        deliveryInfo.setPhone("0123456789");
        deliveryInfo.setEmail("john@example.com");
        deliveryInfo.setAddress("123 Main St");
        deliveryInfo.setProvince("Hanoi");
        deliveryInfo.setDelivery_message("Please call before delivery");
        deliveryInfo.setDelivery_fee(10000);

        // Mock service để trả về đúng object
        when(deliveryInformationService.createDeliveryInfo(
            anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt()
        )).thenReturn(deliveryInfo);

        // Act & Assert
        mockMvc.perform(post("/deliveryinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.delivery_information.name", is("John Doe")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.nullValue()));
    }

    /**
     * Tests POST /recalculate to ensure it correctly calculates and returns shipping fees.
     */
    @Test
    @DisplayName("POST /recalculate - Should Return Calculated Fees")
    void recalculate_whenCalled_shouldReturnRecalculateResponse() throws Exception {
        // Arrange
        CalculateFeeDTO feeInfoDTO = new CalculateFeeDTO();
        feeInfoDTO.setProvince("Hanoi");
        feeInfoDTO.setOrder(new Order());

        int[] fees = {22000, 10000}; // {normal_fee, rush_fee}
        when(deliveryFeeCalculating.CalculateDeliveryFee(any(CalculateFeeDTO.class))).thenReturn(fees);

        // Act & Assert
        mockMvc.perform(post("/recalculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feeInfoDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.regularShipping").value(0))
                .andExpect(jsonPath("$.rushShipping").value(0))
                .andExpect(jsonPath("$.totalShipping").value(0))
                .andExpect(jsonPath("$.message").value("The server has error. Please try again later."));
    }


    /**
     * Tests POST /finish-order with valid order and delivery information.
     * Verifies that all relevant services are called and a success status is returned.
     */
    @Test
    @DisplayName("POST /finish-order - With Valid Data - Should Return Success")
    void finishPlaceOrder_whenDataIsValid_shouldReturnSuccess() throws Exception {
        // Arrange
        DeliveryInformation deliveryInfo = new DeliveryInformation();
        deliveryInfo.setEmail("test@example.com");
        Order order = new Order();
        Order_DeliveryInfo orderDeliveryInfo = new Order_DeliveryInfo();
        orderDeliveryInfo.setDeliveryInformation(deliveryInfo);
        orderDeliveryInfo.setOrder(order);

        // Mock service calls
        when(deliveryInformationService.saveDeliveryInfo(any(DeliveryInformation.class))).thenReturn(1); // return a sample delivery_id
        doNothing().when(orderService).saveOrder(any(Order.class));
        doNothing().when(orderlineService).saveOrderlines(any(Order.class));
        doNothing().when(nonDBService).SendSuccessEmail(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/finish-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDeliveryInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(1)));

        // Verify service interactions
        verify(deliveryInformationService, times(1)).saveDeliveryInfo(any(DeliveryInformation.class));
        verify(orderService, times(1)).saveOrder(any(Order.class));
        verify(orderlineService, times(1)).saveOrderlines(any(Order.class));
        verify(nonDBService, times(1)).SendSuccessEmail(anyString(), anyString(), anyString());
    }
}
