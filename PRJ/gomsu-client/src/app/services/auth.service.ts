import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
const APIURL = environment.APIURL;
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) {}
  private _isLoggedIn: boolean = false;
  login(email: string, password: string): Observable<any> {
    const data = { email, password };
    console.log('data: ', data);
    return this.httpClient
      .post(`${APIURL}users/public/login`, data, {
        withCredentials: true,
        responseType: 'text',
      })
      .pipe();
  }

  getIsLoggedIn(): boolean {
    return this.checkCookie();
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
            resolve(true);
            this.loggedOut();
          },
          error: (error) => {
            console.error('Error during logout', error);
            reject(false);
          },
        });
    });
  }
  register(
    email: string,
    password: string,
    fullName: string,
    phoneNumber: string,
  ): Observable<any> {
    const data = {
      email: email,
      password: password,
      fullName: fullName,
      phone_number: phoneNumber,
    };
    console.log('data: ', data);
    return this.httpClient
      .post(`${APIURL}users/public/register`, data, {
        responseType: 'text',
      })
      .pipe();
  }

  loginWithGoogle(idToken: string): Observable<any> {
    return this.httpClient
      .post(
        `${APIURL}/users/public/oauth2/login/google`,
        { idToken },
        { withCredentials: true },
      )
      .pipe();
  }

  findAccount(email: string): Observable<any> {
    return this.httpClient
      .get(`${APIURL}users/public/forgot/find-account/${email}`, {
        responseType: 'json',
      })
      .pipe();
  }
  sendOTP(email: string): Observable<any> {
    const params = new HttpParams().set('email', email);
    return this.httpClient
      .post(`${APIURL}users/public/request-change-password`, null, {
        params: params,
        responseType: 'text',
      })
      .pipe();
  }

  changePw(userId: string, otp: string, newPassword: string ):Observable<String>{
    const params = new HttpParams()
      .set('userId', userId)
      .set('otpRequest', otp)
      .set('newPassword', newPassword);
    return this.httpClient
    .post(`${APIURL}users/public/change-password`, null, {
      params: params,
      responseType: 'text',
    })
  }
}
