import { Component, Input, OnInit } from '@angular/core';
import {
  faCartShopping,
  faHeart,
  faMagnifyingGlass,
} from '@fortawesome/free-solid-svg-icons';
import { Product } from '../../models/product.model';
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  standalone: false,
})
export class ProductComponent implements OnInit {
  faCartShopping = faCartShopping;
  faHeart = faHeart;
  faMagnifyingGlass = faMagnifyingGlass;
  @Input() product!: Product;
  priceAfterDiscount: number = 0;
  isShowPopup: boolean = false;
  quantity: number = 1;
  constructor() {}

  ngOnInit() {
    this.priceAfterDiscount =
      this.product.price - (this.product.discount * this.product.price) / 100;
  }

  onClickOutside() {
    this.isShowPopup = false;
  }
  handleClosePopup() {
    this.isShowPopup = false;
  }

  handleShowPopup(event: Event) {
    this.isShowPopup = !this.isShowPopup;
  }

  handleDecreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  handleIncreaseQuantity() {
    this.quantity++;
  }
}
