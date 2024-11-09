import { Component, Input, OnInit } from '@angular/core';
import {
  faCartShopping,
  faHeart,
  faMagnifyingGlass,
} from '@fortawesome/free-solid-svg-icons';
import { CookieService } from 'ngx-cookie-service';
import { ToastrService } from 'ngx-toastr';
import { Product } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
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
  constructor(
    private cookieService: CookieService,
    private productService: ProductService,
    private toastr: ToastrService,
  ) {}

  ngOnInit() {
    this.priceAfterDiscount =
      this.product.price - (this.product.discount * this.product.price) / 100;
    console.log(this.priceAfterDiscount);
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
  handleAddToCart() {
    const roleCookie = this.cookieService.get('role');
    console.log('Role: ', roleCookie);
    if (
      roleCookie == 'user' ||
      roleCookie == 'shipper' ||
      roleCookie == 'admin'
    ) {
      const encodedUser = this.cookieService.get('user');
      if (encodedUser) {
        //window.atob(encodedUser): Giải mã chuỗi Base64 thành chuỗi nhị phân
        //escape(...): Chuyển chuỗi nhị phân thành chuỗi an toàn cho UTF-8.
        //decodeURIComponent(...): Giải mã chuỗi an toàn UTF-8 thành chuỗi ký tự có dấu.
        const decodedUser = decodeURIComponent(
          escape(window.atob(encodedUser)),
        );
        //convert to json
        const userObject = JSON.parse(decodedUser);
        console.log('User: ', userObject);
        console.log('Add to cart: ', this.product.id, this.quantity);
        this.productService
          .addProductToCart(this.product.id, this.quantity, userObject.userId)
          .subscribe({
            next: (response) => {
              console.log('Add to cart success: ', response);
              this.toastr.success('Add to cart success');
            },
            error: (error) => {
              console.error('Add to cart error: ', error);
              this.toastr.error('Add to cart error');
            },
          });
      }
    } else {
      this.toastr.error('Please login to add to cart');
    }
  }
}
