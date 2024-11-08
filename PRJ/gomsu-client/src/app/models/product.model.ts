import { Category } from "./category.model";

export interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
  imageUrl: string;
  stock: number;
  category: Category;
  discount: number;
}
