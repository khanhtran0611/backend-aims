import type { Transaction, Invoice } from "./order-types"
import type { Order, DeliveryInformation, Order_server } from "./checkout-types"
import { api } from "./api"
import { CartItem } from "./cart-context"

// Mock function to get order details
export async function getOrderDetails(orderId: number): Promise<{
  order: Order
  delivery: DeliveryInformation
  orderLines: any[]
  transaction: Transaction
  invoice: Invoice
} | null> {
  // Simulate API call

  
  // This would be replaced with actual API call
  try {
      let order = getOrderFromLocalStorage()
      let delivery = getDeliveryFromLocalStorage()
      let orderLines = order.orderLineList
      order.order_id = orderId


    const transaction: Transaction = {
      transaction_id: 3000 + orderId,
      transaction_datetime: new Date().toISOString(),
      transaction_content: "Payment for order #" + orderId,
      amount: order.total_after_VAT, // Total after VAT + shipping
      payment_method: "credit_card",
    }


    const invoice: Invoice = {
      invoice_id: 4000 + orderId,
      order_id: orderId,
      transaction_id: 3000 + orderId,
      description: "Invoice for order #" + orderId,
    }

    console.log(order)
    console.log(delivery)


    return {
      order,
      delivery,
      orderLines,
      transaction,
      invoice,
    }
  } catch (error) {
    console.error("Error fetching order details:", error)
    return null
  }
}

// Format payment method for display
export function formatPaymentMethod(method: string): string {
  switch (method) {
    case "cod":
      return "Cash on Delivery (COD)"
    case "momo":
      return "MoMo E-Wallet"
    case "vnpay":
      return "VNPay"
    default:
      return method
  }
}

// Format date for display
export function formatDate(dateString: string): string {
  const date = new Date(dateString)
  return new Intl.DateTimeFormat("vi-VN", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date)
}

export async function createOrder(cartItems: Omit<CartItem, "selected">[]) {
  const cart = {
    listofProducts: cartItems
  }
  const response = await api.post("/placeorder", cart)
  return response.order
}


export function saveOrderToLocalStorage(order: any) {
  localStorage.setItem("order", JSON.stringify(order))
}

export function getOrderFromLocalStorage() {
  const order = localStorage.getItem("order")
  return order ? JSON.parse(order) : null
}

export function removeOrderFromLocalStorage() {
  localStorage.removeItem("order")
}

export function saveDeliveryToLocalStorage(delivery: DeliveryInformation) {
  localStorage.setItem("delivery", JSON.stringify(delivery))
}

export function getDeliveryFromLocalStorage() {
  const delivery = localStorage.getItem("delivery")
  return delivery ? JSON.parse(delivery) : null
}

export function removeDeliveryFromLocalStorage() {
  localStorage.removeItem("delivery")
}

export async function FinishOrder(order : any, deliveryInfo : DeliveryInformation) {
  
    const response = await api.post("/finish-order", {
      order : order, 
      deliveryInformation : deliveryInfo})
    return response
}

export async function ApproveOrder(orderId : number) {
  const response = await api.post("/api/order/approve?order_id=" + orderId,{})
  return response
}

export async function getOrderById(orderId : number) {
  const response = await api.get("/order-detail?order_id=" + orderId)
  return response
}



