import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment.development';

@Injectable({
    providedIn: 'root',
})
export class ProductService {
    private apiUrl = environment.APIURLProduct;

    constructor(private http: HttpClient) {
    }

    getProducts(page: string, size: string, categoryId: string): Observable<any> {
        const params = new HttpParams()
            .set('page', page)
            .set('size', size)
            .set('categoryId', categoryId);
        return this.http.get<any>(`${this.apiUrl}products`, {params});
    }

    getLimitProducts(limit: string, allProduct: number): Observable<any> {
        let categoryId: number = 0;
        const totalPage = Math.ceil(allProduct / 12);
        const page = Math.ceil(Math.random() * (totalPage - 1 + 1) + 1).toString();
        const params = new HttpParams()
            .set('page', page)
            .set('size', limit)
            .set('categoryId', categoryId.toString());
        return this.http.get<any>(`${this.apiUrl}products`, {params});
    }

    getCountAllProducts(categoryId: Number): Observable<any> {
        const params = new HttpParams().set('categoryId', categoryId.toString());
        return this.http.get<any>(`${this.apiUrl}products/count`, {params});
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
        console.log(product);
        return this.http.post<any>(`${this.apiUrl}cart/add`, product);
    }

    increaseProductQuantity(productId: number, userId: string): Observable<any> {
        const product = {
            productId: productId,
            userId: userId,
        };
        return this.http.put<any>(`${this.apiUrl}cart/product/increase`, product);
    }

    removeProductCart(productId: number, userId: string): Observable<any> {
        const product = {
            productId: productId,
            userId: userId,
        };
        return this.http.put<any>(`${this.apiUrl}cart/product/remove`, product);
    }

    decreaseProductQuantity(productId: number, userId: string): Observable<any> {
        const product = {
            productId: productId,
            userId: userId,
        };
        return this.http.put<any>(`${this.apiUrl}cart/product/decrease`, product);
    }

    getCartByUserId(userId: string): Observable<any> {
        const params = new HttpParams().set('userId', userId);
        return this.http.get<any>(`${this.apiUrl}cart/get`, {params});
    }
}
