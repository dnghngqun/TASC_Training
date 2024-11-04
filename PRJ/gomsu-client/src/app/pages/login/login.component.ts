import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private cookieService: CookieService,
    private router: Router,
    private toastr: ToastrService,
  ) {}
  role!: string;
  email!: string;
  password: string = '';
  private intervalId: any;
  ngOnInit() {
    this.checkLoggedIn();
    this.intervalId = setInterval(() => {
      this.checkLoggedIn();
    }, 500);
  }
  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }

  onClickLogin() {
    console.log('Email: ', this.email);
    console.log('PW: ', this.password);

    this.loginComponent(this.email, this.password);
  }

  loginComponent(email: string, password: string) {
    this.authService.login(email, password).subscribe({
      next: (res) => {
        console.log('Message: ', res);
        this.toastr.success('Login success 🥳');
      },
      error: (error) => {
        console.error('Login error: ', error); // Log lỗi nếu có
        this.toastr.error('Login failed 😡 Please check email and password 🙏');
      },
    });
  }

  private checkLoggedIn(): void {
    const roleCookie = this.cookieService.get('role');
    if (roleCookie === 'user') {
      this.router.navigate(['/']);
    } else if (this.role === 'shipper') {
      console.log('navigate to shipper');
    } else {
      console.log('navigate to admin');
    }
  }

  isDisabledBtn = true;
  isEmailValid!: string;
  errMessage!: string;
  onEmailChange() {
    let pattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
    if (this.email === '') {
      this.isEmailValid = '';
      this.isDisabledBtn = true;
      this.errMessage = '';
    } else if (!this.email.match(pattern)) {
      this.isEmailValid = 'falseEmail';
      this.isDisabledBtn = true;
      this.errMessage = 'Invalid email format!';
    } else if(this.email.match(pattern) && this.password === '') {
      this.isEmailValid = 'trueEmail';
      this.errMessage = '';
      this.isDisabledBtn = true;
    }
    else{
      this.isEmailValid = 'trueEmail';
      this.errMessage = '';
      this.isDisabledBtn = false;
    }
  }
  isPwValid!: string;
  errMessagePw!: string;
  onPasswordChange() {
    if (this.password === '') {
      this.isPwValid = '';
      this.isDisabledBtn = true;

      this.errMessagePw = '';
    } else {
      this.isPwValid = 'truePw';
      this.isDisabledBtn = false;
      this.errMessagePw = '';
    }
  }
}
