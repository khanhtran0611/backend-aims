package controller;

import Project_ITSS.CancelOrder.Controller.CancelOrderController;
import Project_ITSS.CancelOrder.Command.CommandResult;
import Project_ITSS.CancelOrder.Entity.Order;
import Project_ITSS.CancelOrder.Exception.OrderCancellationException;
import Project_ITSS.CancelOrder.Exception.OrderNotFoundException;
import Project_ITSS.CancelOrder.Service.OrderCancellationService;
import Project_ITSS.CancelOrder.Service.OrderService_CancelOrder;
import Project_ITSS.CancelOrder.Service.ProductService_CancelOrder;
import Project_ITSS.Project_ITSSApplication;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the CancelOrderController.
 * This class uses @WebMvcTest to test the controller layer, mocking the service dependencies.
 */
@WebMvcTest(CancelOrderController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
class CancelOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderCancellationService cancellationService;

    @MockBean
    private OrderService_CancelOrder orderService;

    @MockBean
    private ProductService_CancelOrder productService;

    /**
     * Test case for a successful order cancellation.
     * It verifies that a POST request to /api/order/cancel with a valid order ID
     * returns an HTTP 200 OK status and a success message.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void cancelOrder_whenSuccessful_shouldReturnOk() throws Exception {
        // Arrange
        long orderId = 123L;
        CommandResult successResult = CommandResult.success("Order cancelled successfully");
        when(cancellationService.cancelOrder(eq(orderId), any(HttpServletRequest.class)))
                .thenReturn(successResult);

        // Act & Assert
        mockMvc.perform(post("/api/order/cancel").param("order_id", String.valueOf(orderId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Order cancelled successfully"));
    }

    /**
     * Test case for when the order to be canceled is not found.
     * It verifies that the controller returns an HTTP 404 Not Found status.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void cancelOrder_whenOrderNotFound_shouldReturnNotFound() throws Exception {
        // Arrange
        long orderId = 404L;
        when(cancellationService.cancelOrder(eq(orderId), any(HttpServletRequest.class)))
                .thenThrow(new OrderNotFoundException("Order not found: " + orderId));

        // Act & Assert
        mockMvc.perform(post("/api/order/cancel").param("order_id", String.valueOf(orderId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found: " + orderId));
    }

    /**
     * Test case for when an order cannot be canceled (e.g., due to its status).
     * It verifies that the controller returns an HTTP 400 Bad Request status.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void cancelOrder_whenCancellationFails_shouldReturnBadRequest() throws Exception {
        // Arrange
        long orderId = 400L;
        String errorMessage = "Order cannot be cancelled. Only pending orders can be cancelled.";
        when(cancellationService.cancelOrder(eq(orderId), any(HttpServletRequest.class)))
                .thenThrow(new OrderCancellationException(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/order/cancel").param("order_id", String.valueOf(orderId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }

    /**
     * Test case for a successful order approval.
     * Verifies that the endpoint returns a success message when the order is in 'pending' state.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void approveOrder_whenOrderIsPending_shouldReturnSuccess() throws Exception {
        // Arrange
        int orderId = 1;
        Order pendingOrder = new Order();
        pendingOrder.setOrder_id(orderId);
        pendingOrder.setStatus("pending");

        when(orderService.getOrderById(orderId)).thenReturn(pendingOrder);
        doNothing().when(orderService).updateOrderStatusToApprove(orderId);
        doNothing().when(productService).updateProductQuantity(orderId);

        // Act & Assert
        mockMvc.perform(post("/api/order/approve").param("order_id", String.valueOf(orderId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.message").value("Order " + orderId + " has been approved."));

        verify(orderService, times(1)).updateOrderStatusToApprove(orderId);
        verify(productService, times(1)).updateProductQuantity(orderId);
    }

    /**
     * Test case for approving an order that is not found.
     * Verifies that the endpoint returns an error message and status 0.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void approveOrder_whenOrderNotFound_shouldReturnError() throws Exception {
        // Arrange
        int orderId = 999;
        when(orderService.getOrderById(orderId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/order/approve").param("order_id", String.valueOf(orderId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    /**
     * Test case for approving an order that is not in a 'pending' state.
     * Verifies that the endpoint returns an appropriate error message.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void approveOrder_whenStatusNotPending_shouldReturnError() throws Exception {
        // Arrange
        int orderId = 2;
        Order approvedOrder = new Order();
        approvedOrder.setOrder_id(orderId);
        approvedOrder.setStatus("approved");

        when(orderService.getOrderById(orderId)).thenReturn(approvedOrder);

        // Act & Assert
        mockMvc.perform(post("/api/order/approve").param("order_id", String.valueOf(orderId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.message").value("Order status must be 'pending' to approve. Current status: approved"));
    }
    
    /**
     * Test case for the /api/order/test endpoint.
     * It verifies that the endpoint returns the expected string message.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    void testEndpoint_shouldReturnWorkingMessage() throws Exception {
        mockMvc.perform(get("/api/order/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancel Order Controller is working!"));
    }
}
