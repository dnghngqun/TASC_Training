import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {productCartResponse} from '../../dto/productCartResponse.model';
import {DataCartService} from '../../services/dataCart.service';

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

  constructor(
    private router: Router,
    private toastr: ToastrService,
    private dataCartService: DataCartService,
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
}
