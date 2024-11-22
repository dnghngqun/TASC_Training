import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {productCartResponse} from '../dto/productCartResponse.model';
import {environment} from '../../environments/environment.development';
import {Observable} from 'rxjs';

const APIURL = environment.APIURL;

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private httpClient: HttpClient) {
  }

  handlePayment(addressId: number, totalPrice: number, userId: string, productCart: productCartResponse[], paymentMethod: string): Observable<any> {
    const data = {
      userId: userId,
      totalPrice: totalPrice,
      addressId: addressId,
      orderDetails: productCart.map((item) => {
        return {
          productId: item.product.id,
          price: item.product.price,
          quantity: item.quantity
        }
      }),
      paymentMethod: paymentMethod
    }
    return this.httpClient.post<any>(`${APIURL}orders/addOrder`, data);
  };

}
