import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements OnInit {
  email: string = '';
  findedAccount: boolean = false;
  otp: string = '';
  newPass: string = '';
  newPassAgain: string = '';
  userId: string = '';
  phoneNumber: string = '';
  isDisableInputEmail: boolean = false;

  setFindedAccount() {
    this.findedAccount = !this.findedAccount;
  }

  name: string = '';

  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
  ) {
  }

  ngOnInit() {
  }

  isDisabledBtn = true;
  isEmailValid!: string;
  errMessage!: string;

  onEmailChange() {
    let pattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
    if (this.email === '') {
      this.isEmailValid = '';
      this.isDisabledBtn = true;
      this.errMessage = '* Email không được để trống!';
    } else if (!this.email.match(pattern)) {
      this.isEmailValid = 'falseEmail';
      this.isDisabledBtn = true;
      this.errMessage = '* Email không đúng định dạng!';
    } else {
      this.isEmailValid = 'trueEmail';
      this.errMessage = '';
      this.isDisabledBtn = false;
    }
  }

  isDisableBtnChange = true;
  isOTPValid!: string;
  errOTPMessage!: string;
  isNewPWValid!: string;
  isPwAgainValid!: string;
  errPwMessage!: string;

  onOTPChange() {
    if (this.otp === '') {
      this.isOTPValid = '';
      this.isDisableBtnChange = true;
      this.errOTPMessage = '* OTP không được để trống!';
    } else if (
      (this.otp !== '' && this.newPass === '') ||
      (this.otp !== '' && this.newPassAgain === '')
    ) {
      this.isOTPValid = 'trueOTP';
      this.isDisableBtnChange = true;
      this.errOTPMessage = '';
    } else {
      this.isOTPValid = '';
      this.isDisableBtnChange = false;
      this.errOTPMessage = '';
    }
  }

  onNewPWChange() {
    let pattern =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (this.newPass === '') {
      this.isNewPWValid = '';
      this.isDisabledBtn = true;
      this.errPwMessage = '* Mật khẩu không được để trống!';
    } else if (!this.newPass.match(pattern)) {
      this.isNewPWValid = 'falsePw';
      this.isDisabledBtn = true;
      this.errPwMessage =
        '* Mật khẩu tối thiểu 8 ký tự, ít nhất 1 chữ cái và 1 số!';
    } else if (this.newPass !== this.newPassAgain || this.newPassAgain === '') {
      this.isNewPWValid = 'falsePw';
      this.isDisabledBtn = true;
      this.errPwMessage = '* Mật khẩu không khớp!';
    } else {
      this.isNewPWValid = 'truePw';
      this.isDisableBtnChange = false;
      this.errPwMessage = '';
    }
  }

  onNewPwAgainChange() {
    if (this.newPassAgain === '') {
      this.isPwAgainValid = '';
      this.isDisabledBtn = true;
      this.errPwMessage = '* Mật khẩu không được để trống!';
    } else if (this.newPass !== this.newPassAgain) {
      this.isPwAgainValid = 'falsePw';
      this.isDisabledBtn = true;
      this.errPwMessage = '* Mật khẩu không khớp!';
    } else {
      this.isPwAgainValid = 'truePw';
      this.isDisableBtnChange = false;
      this.errPwMessage = '';
      this.isNewPWValid = 'truePw';
    }
  }

  onFindAccount() {
    this.authService.findAccount(this.email).subscribe({
      next: (res) => {
        console.log('res: ', res);
        this.name = res.name;
        this.phoneNumber = res.phoneNumber;
        this.userId = res.userId;
        this.findedAccount = true;
        this.isDisableInputEmail = true;
      },
      error: (error) => {
        console.error('error: ', error);
        this.toastr.error('Email không tồn tại!');
      },
    });
  }

  onClickNotAccount() {
    this.email = '';
    this.findedAccount = false;
    this.isDisableInputEmail = false;
  }

  onClickSendOTP() {
    this.toastr.info('Đang gửi mã OTP...');
    this.authService.sendOTP(this.email).subscribe({
      next: (res) => {
        console.log('res: ', res);
        this.toastr.success('Mã OTP đã được gửi đến email của quý khách!');
      },
      error: (err) => {
        console.error('err: ', err);
        this.toastr.error('Gửi mã OTP thất bại!');
      },
    });
  }

  onClickChangePW() {
    this.toastr.info('Đang thay đổi mật khẩu...');
    this.authService.changePw(this.userId, this.otp, this.newPass).subscribe({
      next: (res) => {
        console.log('res: ', res);
        this.toastr.success('Thay đổi mật khẩu thành công 🎉');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log('err: ', err);
        this.toastr.error(`Thay đổi mật khẩu thất bại! ${err.error} 😭`);
      },
    });
  }
}
