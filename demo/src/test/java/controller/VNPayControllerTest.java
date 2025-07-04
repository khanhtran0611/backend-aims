package controller;

import Project_ITSS.vnpay.common.controller.VNPayController;
import Project_ITSS.vnpay.common.dto.PaymentRequest;
import Project_ITSS.vnpay.common.dto.QueryRequest;
import Project_ITSS.vnpay.common.dto.RefundRequest;
import Project_ITSS.vnpay.common.observer.PaymentSubject;
import Project_ITSS.vnpay.common.service.HashService;
import Project_ITSS.vnpay.common.service.OrderService;
import Project_ITSS.vnpay.common.service.PaymentService;
import Project_ITSS.vnpay.common.service.VNPayService;
import Project_ITSS.Project_ITSSApplication;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the VNPayController.
 * This class uses @WebMvcTest to test the controller layer by mocking its service dependencies.
 */
@WebMvcTest(VNPayController.class)
@ContextConfiguration(classes = Project_ITSSApplication.class)
class VNPayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock all service dependencies of VNPayController
    @MockBean
    @Qualifier("vnpayService")
    private PaymentService paymentService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private HashService hashService;
    @MockBean
    private PaymentSubject paymentSubject;

    /**
     * Tests the POST /api/payment endpoint for creating a new payment.
     */
    @Test
    @DisplayName("POST /api/payment - With Valid Data - Should Return Payment URL")
    void createPayment_whenRequestIsValid_shouldReturnPaymentResponse() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount("100000");
        paymentRequest.setBankCode("NCB");

        VNPayService.PaymentResponse mockResponse = VNPayService.PaymentResponse.builder()
                .code("00")
                .message("success")
                .paymentUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=123")
                .build();

        when(paymentService.createPayment(any(PaymentRequest.class), any(HttpServletRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("00")))
                .andExpect(jsonPath("$.paymentUrl", startsWith("https://sandbox.vnpayment.vn")));
    }

    /**
     * Tests the POST /api/payment/query endpoint.
     */
    @Test
    @DisplayName("POST /api/payment/query - With Valid Data - Should Return Query Response")
    void queryTransaction_whenRequestIsValid_shouldReturnQueryResponse() throws Exception {
        // Arrange
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setOrderId("12345");
        queryRequest.setTransDate("20250626103000");

        VNPayService.QueryResponse mockResponse = new VNPayService.QueryResponse();
        mockResponse.setVnp_ResponseCode("00");
        mockResponse.setVnp_Message("Success");

        when(paymentService.queryTransaction(any(QueryRequest.class), any(HttpServletRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/payment/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vnp_ResponseCode", is("00")));
    }

    /**
     * Tests the POST /api/payment/refund endpoint.
     */
    @Test
    @DisplayName("POST /api/payment/refund - With Valid Data - Should Return Refund Response")
    void refundTransaction_whenRequestIsValid_shouldReturnRefundResponse() throws Exception {
        // Arrange
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId("12345");

        VNPayService.RefundResponse mockResponse = new VNPayService.RefundResponse();
        mockResponse.setVnp_ResponseCode("00");
        mockResponse.setVnp_Message("Success");

        when(paymentService.refundTransaction(any(RefundRequest.class), any(HttpServletRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/payment/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vnp_ResponseCode", is("00")));
    }


    /**
     * Tests the GET /vnpay/return endpoint with a successful payment and valid hash.
     * Verifies that the controller correctly processes the payment and redirects.
     */
    @Test
    @DisplayName("GET /vnpay/return - With Successful Payment and Valid Hash - Should Process and Redirect")
    void vnpayReturn_whenSuccessAndValidHash_shouldProcessAndRedirect() throws Exception {
        // Arrange
        String orderId = "TXN123";
        String secureHash = "validhash123";

        // Mock the hash service to return a valid hash
        when(hashService.hashAllFields(anyMap())).thenReturn(secureHash);
        doNothing().when(orderService).updateOrderStatus(anyString(), anyString());
        doNothing().when(orderService).saveTransactionInfo(anyString(), anyMap());
        doNothing().when(paymentSubject).notifyPaymentSuccess(anyString(), any());

        // Act & Assert
        mockMvc.perform(get("/vnpay/return")
                        .param("vnp_ResponseCode", "00")
                        .param("vnp_TxnRef", orderId)
                        .param("vnp_SecureHash", secureHash)
                        .param("vnp_TransactionNo", "VNPTXN001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:3000/order-confirmation/" + orderId));

        // Verify that the correct services were called
        verify(orderService, times(1)).updateOrderStatus(orderId, "PAID");
        verify(orderService, times(2)).saveTransactionInfo(eq(orderId), anyMap()); // Called twice: once in processSuccessfulPayment, once in vnpayReturnPage
        verify(paymentSubject, times(1)).notifyPaymentSuccess(eq(orderId), any());
    }

    /**
     * Tests the GET /vnpay/return endpoint with an invalid hash.
     * Verifies that the controller does not process the payment and still redirects.
     */
    @Test
    @DisplayName("GET /vnpay/return - With Invalid Hash - Should Not Process and Redirect")
    void vnpayReturn_whenInvalidHash_shouldNotProcessAndRedirect() throws Exception {
        // Arrange
        String orderId = "TXN456";
        String secureHash = "validhash456";
        String invalidHash = "invalidhash";

        // Mock the hash service to return a non-matching hash
        when(hashService.hashAllFields(anyMap())).thenReturn(secureHash);

        // Act & Assert
        mockMvc.perform(get("/vnpay/return")
                        .param("vnp_ResponseCode", "00")
                        .param("vnp_TxnRef", orderId)
                        .param("vnp_SecureHash", invalidHash)) // Use the invalid hash
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:3000/order-confirmation/" + orderId));

        // Verify that the payment processing services were NOT called for updateOrderStatus and notifyPaymentSuccess
        // However, saveTransactionInfo is still called in vnpayReturnPage regardless of hash validity
        verify(orderService, never()).updateOrderStatus(anyString(), anyString());
        verify(orderService, times(1)).saveTransactionInfo(anyString(), anyMap()); // Still called once in vnpayReturnPage
        verify(paymentSubject, never()).notifyPaymentSuccess(anyString(), any());
    }

    /**
     * Tests the GET /vnpay/return endpoint with a failed payment response code.
     * Verifies that the controller processes the failure and redirects.
     */
    @Test
    @DisplayName("GET /vnpay/return - With Failed Payment - Should Process Failure and Redirect")
    void vnpayReturn_whenPaymentFailed_shouldProcessFailureAndRedirect() throws Exception {
        // Arrange
        String orderId = "TXN789";
        String secureHash = "validhash789";
        String failureCode = "24"; // Customer cancel transaction code

        when(hashService.hashAllFields(anyMap())).thenReturn(secureHash);
        doNothing().when(orderService).updateOrderStatus(anyString(), anyString());
        doNothing().when(paymentSubject).notifyPaymentFailed(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(get("/vnpay/return")
                        .param("vnp_ResponseCode", failureCode)
                        .param("vnp_TxnRef", orderId)
                        .param("vnp_SecureHash", secureHash))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:3000/")); // Redirects to home on failure

        // Verify that failure processing services were called
        verify(orderService, times(1)).updateOrderStatus(orderId, "FAILED");
        verify(paymentSubject, times(1)).notifyPaymentFailed(orderId, "Payment failed with code: " + failureCode);
        verify(orderService, never()).saveTransactionInfo(anyString(), anyMap());
    }
}
