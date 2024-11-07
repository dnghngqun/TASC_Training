import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = environment.APIURL;

  constructor(private http: HttpClient) { }

  getProducts(page:string, size:string):Observable<Object>{
    const params = new HttpParams()
    .set('page', page)
    .set('size', size);
    return this.http.get<Object>(`${this.apiUrl}products`,{params});
  }

  getCountAllProducts():Observable<Object>{
    return this.http.get<Object>(`${this.apiUrl}products/count`);
  }

}
