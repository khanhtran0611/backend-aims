import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"
import { api, fileApi } from "./api"
import { type } from "os"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

// Format price in Vietnam Dong
export function formatCurrency(price: number): string {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0,
  }).format(price)
}



// Mock data function to simulate fetching products
export async function fetchProducts(
  page = 1,
  limit = 10,
): Promise<{
  products: Product[]
  totalPages: number
}> {
  // Simulate API delay
  await new Promise((resolve) => setTimeout(resolve, 500))
  let response = await api.get("/product/all");
  // Specific mock products with realistic data
  // Calculate pagination
  const startIndex = (page - 1) * limit
  const endIndex = startIndex + limit
  const paginatedProducts = response.slice(startIndex, endIndex)
  const totalPages = Math.ceil(response.length / limit)

  return {
    products: paginatedProducts,
    totalPages,
  }
}

// Fetch individual product by ID with enhanced details
export async function fetchProductById(id: number, type: string): Promise<Product | Book | DVD | CD | null> {
  // Simulate API delay
  await new Promise((resolve) => setTimeout(resolve, 500))
  console.log(type)
  let response = await api.get(`/product/all-detail/${id}?type=${type}`);
  // Get base product data
  return response as Product | Book | DVD | CD | null;
}


export async function getAllProducts(): Promise<Product[]> {
  let response = await api.get(`/product/all`);
  return response;
}

export async function editProduct(product: any) {
  console.log("product", product)
  let response = await api.post("/updating/ProductInfo", product);
  return response;
}

export async function addProduct(product: any) {
  console.log("Adding product:", product)
  let response = await api.post("/adding/ProductInfo", product);
  return response;
}

export async function checkAddProductAvailable() {
  // This API checks if adding a product is currently available
  return await api.get("/adding/available")
}

export async function checkUpdateProductAvailable() {
  // This API checks if updating a product is currently available
  return await api.get("/updating/available")
}

export async function uploadImage(file: File) {
  // This API uploads an image to the backend for processing and saving
  const formData = new FormData()
  formData.append('image', file)
  return await fileApi.post("/upload-image", formData)
}

// API to delete an image, sending filename as request param
export async function deleteImage(filename: string) {
  return await api.post(`/delete-image?image=${encodeURIComponent(filename)}`, {})
}



export type Product = {
  product_id: number
  title: string
  type: "book" | "dvd" | "cd"
  price: number
  weight: number
  rush_order_supported: boolean
  image_url: string
  barcode: string
  import_date: string
  introduction: string
  quantity: number
}

export type Book = Product & {
  book_id: number
  genre: string
  page_count: number
  publication_date: string
  authors: string
  publishers: string
  cover_type: "hardcover" | "paperback"
}

export type DVD = Product & {
  dvd_id: number
  release_date: string
  dvd_type: "Blu-ray" | "Standard DVD"
  genre: string
  studio: string
  director: string
}

export type CD = Product & {
  cd_id: number
  track_list: string
  genre: string
  record_label: string
  artists: string
  release_date: string
}
