import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private cookieService: CookieService,
    private router: Router,
    private toastr: ToastrService,
  ) {}
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
  role: string = '';
  email: string = '';
  password!: string;
  fullName: string = '';
  phoneNumber: string = '';
  onClickRegister() {
    console.log('Email: ', this.email);
    console.log('PW: ', this.password);
    console.log('Full name: ', this.fullName);
    console.log('Phone number: ', this.phoneNumber);
    this.authService
      .register(this.email, this.password, this.fullName, this.phoneNumber)
      .subscribe({
        next: (res) => {
          console.log('Message: ', res);
          this.toastr.success('Register success 🥳');
          this.authService.login(this.email, this.password).subscribe({
            next: (res) => {
              console.log('Login success!');
            },
          });
        },
        error: (error) => {
          console.error('Register error: ', error);
          this.toastr.error('Register failed 😡 Please check information🙏');
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
      this.errMessage = '* Email không được để trống!';
    } else if (!this.email.match(pattern)) {
      this.isEmailValid = 'falseEmail';
      this.errMessage = '* Email không đúng định dạng!';
    } else {
      this.isEmailValid = 'trueEmail';
      this.errMessage = '';
    }
    this.checkAllFields();
  }
  isPwValid!: string;
  errMessagePw!: string;
  onPasswordChange() {
    let pattern =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (this.password === '') {
      this.isPwValid = '';
      this.errMessagePw = '* Mật khẩu Không được để trống!';
    } else if (!this.password.match(pattern)) {
      this.isPwValid = 'falsePw';
      this.errMessagePw =
        '* Mật khẩu tối thiểu 8 ký tự, ít nhất 1 chữ cái và 1 số!';
    } else {
      this.isPwValid = 'truePw';
      this.errMessagePw = '';
    }
    this.checkAllFields();
  }

  isFullNameValid!: string;
  errMessageFullName!: string;

  onFullNameChange() {
    if (this.fullName === '') {
      this.isFullNameValid = '';
      this.errMessageFullName = '* Không được để trống trường này!';
    } else {
      this.isFullNameValid = 'trueFullName';
      this.errMessageFullName = '';
    }
    this.checkAllFields();
  }

  isPhoneNumberValid!: string;
  errMessagePhoneNumber!: string;
  onPhoneNumberChange() {
    let phonePattern = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;
    if (this.phoneNumber === '') {
      this.isPhoneNumberValid = '';
      this.errMessagePhoneNumber = '* Số điện thoại không được để trống!';
    } else if (!this.phoneNumber.match(phonePattern)) {
      this.isPhoneNumberValid = 'falsePhoneNumber';
      this.errMessagePhoneNumber = '* Số điện thoại không hợp lệ!';
    } else {
      this.isPhoneNumberValid = 'truePhoneNumber';
      this.errMessagePhoneNumber = '';
    }
    this.checkAllFields();
  }

  private checkAllFields() {
    this.isDisabledBtn = !(
      this.isEmailValid === 'trueEmail' &&
      this.isPwValid === 'truePw' &&
      this.isFullNameValid === 'trueFullName' &&
      this.isPhoneNumberValid === 'truePhoneNumber'
    );
  }
}
