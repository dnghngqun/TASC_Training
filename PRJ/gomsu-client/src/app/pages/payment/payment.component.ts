import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit {
  @Input() totalPriceTemporary: number = 0;
  @Input() productCart: any[] = [];
  discount: number = 0;
  totalValue: number = this.totalPriceTemporary - this.discount;
  errDiscount: string = "";
  totalCart: number = this.productCart.length;

  constructor() {
  }

  ngOnInit() {

  }
}
