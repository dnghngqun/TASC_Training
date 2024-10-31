import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from '../../services/login_service.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(
    private loginService: LoginService,
    private cookieService: CookieService,
    private router: Router,
  ) {}
  role!: string;
  email!: string;
  password!: string;
  ngOnInit() {}

  onClickLogin() {
    console.log('Email: ', this.email);
    console.log('PW: ', this.password);
    this.loginService.login(this.email, this.password).subscribe((res) => {
      console.log('Message: ', res);
      const jwtCookie = this.cookieService.get('jwt');
      const roleCookie = this.cookieService.get('role');
      const userCookie = this.cookieService.get('user');
      if (jwtCookie != null && roleCookie != null && userCookie != null) {
        this.role = roleCookie;
        this.loginService.loggedIn();
        if (this.role === 'user') {
          this.router.navigate(['/']);
        } else if (this.role === 'shipper') {
          console.log('navigate to shipper');
        } else {
          console.log('navigate to admin');
        }
      }
    });
  }
}
