import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {productCartResponse} from '../../dto/productCartResponse.model';
import {DataCartService} from '../../services/dataCart.service';
import {PaymentService} from '../../services/payment.service';
import {EncryptionService} from '../../services/encryption.service';
import {CookieService} from 'ngx-cookie-service';
import {User} from '../../models/user.model';
import Swal from "sweetalert2";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit {
  totalPriceTemporary: number = 0;
  productCart: productCartResponse[] = [];
  discount: number = 0;
  totalValue: number = 0;
  errDiscount: string = "";
  totalCart: number = 0;
  addressId!: number;
  paymentMethod!: string;
  isClickBtnPayment: boolean = false;
  userId!: string;
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private toastr: ToastrService,
    private dataCartService: DataCartService,
    private paymentService: PaymentService,
    private encryptionService: EncryptionService,
    private cookieService: CookieService
  ) {
  }

  ngOnInit() {
    const receivedData = this.dataCartService.getData();
    if (receivedData == null) {
      this.toastr.info("Bạn không có sản phẩm nào trong giỏ hàng 🥲");
      this.router.navigate(['/']);
    }
    console.log("Received data: ", receivedData);
    this.productCart = receivedData.productCart;
    this.totalPriceTemporary = receivedData.totalPrice;
    this.totalValue = this.totalPriceTemporary - this.discount;
    this.totalCart = this.productCart.length;
  }


  handlePayment() {
    this.isLoading = true;
    this.addressId = 1;
    this.paymentMethod = "online";
    const width = 1000;
    const height = 700;
    const left = window.innerWidth / 2 - width / 2;
    const top = window.innerHeight / 2 - height / 2;
    const encodedUser = this.cookieService.get('user');
    if (encodedUser) {
      const user: User = this.encryptionService.decodeObject(
        encodedUser,
      ) as User;
      if (user != null) {
        this.userId = user.id;
      }
    }
    this.paymentService.handlePayment(
      this.addressId,
      this.totalValue,
      this.userId,
      this.productCart,
      this.paymentMethod
    ).subscribe({
      next: (res) => {
        console.log("res: ", res);
        const payUrl = res.data;
        const popup = window.open(payUrl, "_blank", `width=${width},height=${height},left=${left},top=${top}`);
        if (!popup) {
          alert("Popup bị chặn. Hãy kiểm tra cài đặt trình duyệt.");
        } else {
          window.addEventListener('message', (event) => {
            if (event.origin !== 'http://localhost:8088') {
              console.warn('Event từ nguồn không xác định:', event.origin);
              return;
            }

            console.log("Event data: ", event.data);
            const paymentStatus = event.data.paymentStatus; // success | error

            if (paymentStatus === 'success') {
              console.log('Thanh toán thành công!');
              this.isLoading = false;
              this.router.navigate(['/']);
            } else if (paymentStatus === 'error') {
              this.isLoading = false;
              console.log('Thanh toán thất bại!');
            }
            // close popup if popup is open
            popup.close();
          });
        }

      },
      error: err => {
        console.log("err to payment: ", err);
      },

    })
  }
}
