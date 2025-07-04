package controller;

import Project_ITSS.PayOrder.PayOrderController;
import Project_ITSS.Project_ITSSApplication;
import Project_ITSS.vnpay.common.dto.PaymentRequest;
import Project_ITSS.vnpay.common.service.PaymentService;
import Project_ITSS.vnpay.common.service.VNPayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the PayOrderController.
 * This class uses @WebMvcTest to test the controller layer, mocking the PaymentService dependency.
 */
@WebMvcTest(PayOrderController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
@DisplayName("PayOrderController Tests")
class PayOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Qualifier("vnpayService")
    private PaymentService paymentService;

    /**
     * Test case for creating a payment with valid data.
     * It verifies that the controller calls the PaymentService and returns a successful
     * response with the VNPay payment URL.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    @DisplayName("POST /payment | With Valid Data | Should Return Payment URL")
    void createPayment_withValidData_shouldReturnPaymentUrl() throws Exception {
        // Arrange
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("amount", "150000");
        paymentData.put("language", "vn");

        VNPayService.PaymentResponse mockResponse = VNPayService.PaymentResponse.builder()
                .code("00")
                .message("success")
                .paymentUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=12345")
                .build();

        // Mock the paymentService to return the successful response
        when(paymentService.createPayment(any(PaymentRequest.class), any(HttpServletRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/payorder/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("00")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.paymentUrl", startsWith("https://sandbox.vnpayment.vn")));
    }

    /**
     * Test case for creating a payment with missing data (e.g., amount).
     * The controller is expected to handle this gracefully. In this implementation,
     * it will pass an empty string to the service, which might be handled downstream.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    @DisplayName("POST /payment | With Missing Amount | Should Still Process")
    void createPayment_withMissingAmount_shouldStillProcess() throws Exception {
        // Arrange
        Map<String, Object> paymentData = new HashMap<>();
        // Amount is intentionally omitted
        paymentData.put("language", "en");

        VNPayService.PaymentResponse mockResponse = VNPayService.PaymentResponse.builder()
                .code("00")
                .message("success")
                .paymentUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=54321")
                .build();

        // The controller uses getOrDefault("amount", ""), so the service will be called with an empty amount
        when(paymentService.createPayment(any(PaymentRequest.class), any(HttpServletRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/payorder/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("00")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.paymentUrl", startsWith("https://sandbox.vnpayment.vn")));
    }

    /**
     * Test case for when the downstream payment service returns an error.
     * The controller should propagate the error response from the service.
     *
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @Test
    @DisplayName("POST /payment | When Service Fails | Should Return Error Response")
    void createPayment_whenServiceFails_shouldReturnErrorResponse() throws Exception {
        // Arrange
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("amount", "200000");
        paymentData.put("language", "vn");

        VNPayService.PaymentResponse errorResponse = VNPayService.PaymentResponse.builder()
                .code("99")
                .message("Unknown error")
                .paymentUrl(null)
                .build();

        when(paymentService.createPayment(any(PaymentRequest.class), any(HttpServletRequest.class))).thenReturn(errorResponse);

        // Act & Assert
        mockMvc.perform(post("/payorder/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentData)))
                .andExpect(status().isOk()) // The controller returns OK, but the body contains the error
                .andExpect(jsonPath("$.code", is("99")))
                .andExpect(jsonPath("$.message", is("Unknown error")));
    }
}
