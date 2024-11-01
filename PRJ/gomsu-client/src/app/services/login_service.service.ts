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
    return this.checkCookie();
  }
  loggedIn() {
    localStorage.setItem('isLoggedIn', 'true');
  }

  loggedOut() {
    localStorage.removeItem('isLoggedIn');
  }



  private checkCookie(): boolean {
    return document.cookie
      .split(';')
      .some((item) => item.trim().startsWith('isLoggedIn' + '='));
  }
  logout(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .get(`${APIURL}users/logout`, {
          withCredentials: true,
          responseType: 'text',
        })
        .subscribe({
          next: (response) => {
            console.log('Logout successful', response);
            this.loggedOut();
            resolve(true);
          },
          error: (error) => {
            console.error('Error during logout', error);
            reject(false);
          },
        });
    });
  }

}
