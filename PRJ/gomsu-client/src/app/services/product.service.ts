import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = environment.APIURLProduct;

  constructor(private http: HttpClient) {}

  getProducts(page: string, size: string, categoryId: string): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('categoryId', categoryId);
    return this.http.get<any>(`${this.apiUrl}products`, { params });
  }

  getLimitProducts(limit: string): Observable<any> {
    const limitRequest = parseInt(limit);
    const params = new HttpParams().set('limit', limitRequest);
    return this.http.get<any>(`${this.apiUrl}products/limit`, { params });
  }

  getCountAllProducts(categoryId: Number): Observable<any> {
    const params = new HttpParams().set('categoryId', categoryId.toString());
    return this.http.get<any>(`${this.apiUrl}products/count`, { params });
  }

  getCategories(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}categories/`);
  }

  addProductToCart(
    productId: number,
    quantity: number,
    userId: string,
  ): Observable<any> {
    const product = {
      productId: productId,
      quantity: quantity,
      userId: userId,
    };
    return this.http.post<any>(`${this.apiUrl}cart/add`, product);
  }

  updateQuantityProductInCart(
    productId: number,
    quantity: number,
    userId: string,
  ): Observable<any> {
    const updateProduct = {
      productId: productId,
      quantity: quantity,
      userId: userId,
    };
    return this.http.put<any>(`${this.apiUrl}cart/update/quantity`, {
      updateProduct,
    });
  }

  getCartByUserId(userId: string): Observable<any> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<any>(`${this.apiUrl}cart/get`, { params });
  }
}
