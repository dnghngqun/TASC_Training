import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = environment.APIURLProduct;

  constructor(private http: HttpClient) { }

  getProducts(page:string, size:string, categoryId:string):Observable<any>{
    const params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('categoryId', categoryId);
    return this.http.get<any>(`${this.apiUrl}products`,{params});
  }

  getCountAllProducts(categoryId:Number):Observable<any>{
    const params = new HttpParams()
    .set('categoryId', categoryId.toString());
    return this.http.get<any>(`${this.apiUrl}products/count`,{params});
  }

  getCategories():Observable<any>{
    return this.http.get<any>(`${this.apiUrl}categories/`);
  }

}
