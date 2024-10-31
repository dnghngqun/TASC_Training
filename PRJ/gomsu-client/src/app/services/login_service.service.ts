import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
const APIURL = environment.APIURL;

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private httpClient: HttpClient) {}
  private _isLoggedIn: boolean = false;
  login(email: string, password: string): Observable<any> {
    const data = { email, password };
    console.log('data: ', data);
    return this.httpClient
      .post(`${APIURL}users/signin`, data, {
        withCredentials: true,
        responseType: 'text',
      })
      .pipe();
  }

  getIsLoggedIn(): boolean {
    return localStorage.getItem('isLoggedIn') === 'true' || this.checkCookie();
  }
  loggedIn() {
    localStorage.setItem('isLoggedIn', 'true');
  }

  logggedOut() {
    localStorage.removeItem('isLoggedIn');
  }

  private checkCookie(): boolean {
    // Kiểm tra sự tồn tại của cookie
    return document.cookie.split(';').some((item) => item.trim().startsWith('isLoggedIn' + '='));
  }
  logout(): void {
    this.httpClient
      .get(`${APIURL}users/logout`, {
        withCredentials: true,
        responseType: 'text',
      })
      .pipe();
    this.logggedOut();
  }
}
