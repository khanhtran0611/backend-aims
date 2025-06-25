import type { OrderTrackingInfo, CancellationRequest } from "./tracking-types"
import { api } from "./api"

// Mock function to get order tracking information
export async function getOrderTracking(trackingCode: string): Promise<OrderTrackingInfo | null> {
  // Simulate API call
  const response = await api.get("/order-detail?order_id=" + trackingCode)
  return response
 
}

// Real API function to cancel order - integrated with Java Spring backend
export async function cancelOrder(
  cancellationRequest: Pick<CancellationRequest, "order_id" | "tracking_code">,
) {
  try {
    // Call the real backend API
    const response = await api.post(`/api/order/cancel?order_id=${cancellationRequest.order_id}`, {})

    // Check if the response indicates success
    return response
    
  } catch (error: any) {
    console.error("Error cancelling order:", error)

    // Handle specific error cases
    if (error.message?.includes("Order not found")) {
      return {
        success: false,
        message: "Không tìm thấy đơn hàng."
      }
    }

    if (error.message?.includes("Order cannot be cancelled")) {
      return {
        success: false,
        message: "Đơn hàng không thể hủy. Chỉ có thể hủy đơn hàng đang chờ xử lý."
      }
    }

    if (error.message?.includes("No transaction found")) {
      return {
        success: false,
        message: "Không tìm thấy giao dịch thanh toán cho đơn hàng này. Không thể hoàn tiền."
      }
    }

    return {
      success: false,
      message: "Có lỗi xảy ra khi hủy đơn hàng. Vui lòng thử lại sau."
    }
  }
}

// Update formatTrackingStatus to only handle the 4 statuses
export function formatTrackingStatus(status: string): string {
  switch (status) {
    case "pending":
      return "Pending"
    case "approved":
      return "Approved"
    case "rejected":
      return "Rejected"
    case "cancelled":
      return "Cancelled"
    default:
      return status
  }
}

// Update getStatusColor to only handle the 4 statuses
export function getStatusColor(status: string): string {
  switch (status) {
    case "pending":
      return "bg-yellow-50 text-yellow-700 hover:bg-yellow-50"
    case "approved":
      return "bg-green-100 text-green-800"
    case "rejected":
      return "bg-red-100 text-red-800"
    case "cancelled":
      return "bg-gray-100 text-gray-800"
    default:
      return "bg-gray-100 text-gray-800"
  }
}

// Format date for display
export function formatTrackingDate(dateString: string): string {
  try {
    const date = new Date(dateString)

    // Check if the date is valid
    if (isNaN(date.getTime())) {
      return "Ngày không hợp lệ"
    }

    return new Intl.DateTimeFormat("vi-VN", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }).format(date)
  } catch (error) {
    console.error("Error formatting date:", error, "Date string:", dateString)
    return "Ngày không hợp lệ"
  }
}
