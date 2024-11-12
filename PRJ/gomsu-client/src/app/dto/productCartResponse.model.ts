import { Product } from "../models/product.model";

export interface productCartResponse {
  product: Product
  quantity: number
}
