// Delivery Information interface
export interface DeliveryInformation {
  delivery_id?: number
  name: string
  phone: string
  email: string
  address: string
  province: string
  delivery_message?: string
  delivery_fee: number
}


export interface Order {
  order_id?: number
  delivery_id: number
  total_before_vat: number
  total_after_vat: number
  status: string
  vat: number
}

export interface Order_server{
  order_id: number,
  delivery_id: number,
  total_before_vat: number,
  total_after_vat: number,
  status: string,
  vat: number,
  orderlineList : any[]
}

// Simplified checkout form data interface for frontend
export interface CheckoutFormData {
  deliveryInfo: Omit<DeliveryInformation, "delivery_id" | "delivery_fee">
  orderLineList: Array<{
    product_id: number
    status: "pending"
    quantity: number
    total_fee: number
    delivery_time: string | null
    instructions: string | null
    rush_order_using: boolean
  }>
  paymentMethod: "cod" | "momo" | "vnpay"
  // Order totals for backend
  status: "pending"
  total_after_VAT: number
  total_before_VAT: number
  vat: number
}

// Shipping calculation result
export interface ShippingCalculation {
  regularShipping: number
  rushShipping: number
  freeShippingDiscount: number
  totalShipping: number
}

// Province options for Vietnam
export const VIETNAM_PROVINCES = [
  "An Giang",
  "Ba Ria - Vung Tau",
  "Bac Giang",
  "Bac Kan",
  "Bac Lieu",
  "Bac Ninh",
  "Ben Tre",
  "Binh Dinh",
  "Binh Duong",
  "Binh Phuoc",
  "Binh Thuan",
  "Ca Mau",
  "Cao Bang",
  "Dak Lak",
  "Dak Nong",
  "Dien Bien",
  "Dong Nai",
  "Dong Thap",
  "Gia Lai",
  "Ha Giang",
  "Ha Nam",
  "Ha Tinh",
  "Hai Duong",
  "Hau Giang",
  "Hoa Binh",
  "Hung Yen",
  "Khanh Hoa",
  "Kien Giang",
  "Kon Tum",
  "Lai Chau",
  "Lam Dong",
  "Lang Son",
  "Lao Cai",
  "Long An",
  "Nam Dinh",
  "Nghe An",
  "Ninh Binh",
  "Ninh Thuan",
  "Phu Tho",
  "Quang Binh",
  "Quang Nam",
  "Quang Ngai",
  "Quang Ninh",
  "Quang Tri",
  "Soc Trang",
  "Son La",
  "Tay Ninh",
  "Thai Binh",
  "Thai Nguyen",
  "Thanh Hoa",
  "Thua Thien Hue",
  "Tien Giang",
  "Tra Vinh",
  "Tuyen Quang",
  "Vinh Long",
  "Vinh Phuc",
  "Yen Bai",
  "Phu Yen",
  "Can Tho",
  "Da Nang",
  "Hai Phong",
  "Hanoi",
  "Ho Chi Minh City"
];


// Time slots for rush delivery (2-hour windows)
export const RUSH_DELIVERY_TIME_SLOTS = [
  "08:00 - 10:00",
  "10:00 - 12:00",
  "12:00 - 14:00",
  "14:00 - 16:00",
  "16:00 - 18:00",
  "18:00 - 20:00",
]
