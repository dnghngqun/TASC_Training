import {Component, OnDestroy, OnInit} from '@angular/core';
import {faChevronDown} from '@fortawesome/free-solid-svg-icons';
import {CookieService} from 'ngx-cookie-service';
import {ToastrService} from 'ngx-toastr';
import {productCartResponse} from '../../dto/productCartResponse.model';
import {User} from '../../models/user.model';
import {AuthService} from '../../services/auth.service';
import {EncryptionService} from '../../services/encryption.service';
import {ProductService} from '../../services/product.service';
import {DataCartService} from '../../services/dataCart.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit, OnDestroy {
  productCart: productCartResponse[] = [];
  totalPrice: number = 0;
  totalCart: number = 0;
  datePicker!: Date;

  timePicker: String = '';
  minDate: string = '';
  faChevronDown = faChevronDown;
  private intervalId: any;

  constructor(
    private toastr: ToastrService,
    private productService: ProductService,
    private cookieService: CookieService,
    private encryptionService: EncryptionService,
    private authService: AuthService,
    private router: Router,
    private dataCartService: DataCartService
  ) {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0'); // Tháng bắt đầu từ 0
    const day = today.getDate().toString().padStart(2, '0');
    this.minDate = `${year}-${month}-${day}`;
  }

  ngOnInit() {
    this.updateLoginStatus();
    this.intervalId = setInterval(() => {
      this.updateLoginStatus();
    }, 500);
  }

  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }

  isLoggedIn: boolean = false;

  private updateLoginStatus() {
    this.isLoggedIn = this.authService.getIsLoggedIn();
    if (this.isLoggedIn) {
      this.handleGetProductFromCart();
    }
  }

  handleClickPayment() {
    const dataSent = {
      productCart: this.productCart,
      totalPrice: this.totalPrice
    }
    this.dataCartService.setData(dataSent);
    this.router.navigate(['/payment']);
  }

  handleDecreaseQuantity(productId: number) {
    const encodedUser = this.cookieService.get('user');
    if (encodedUser) {
      const user: User = this.encryptionService.decodeObject(
        encodedUser,
      ) as User;

      this.productService
        .decreaseProductQuantity(productId, user.id)
        .subscribe({
          next: (response) => {
            this.toastr.success('Decrease quantity success');
            console.log('Decrease quantity success: ', response);
          },
          error: (error) => {
            console.error('Decrease error: ', error);
            this.toastr.error('Decrease error');
          },
        });
    }
  }

  handleIncreaseQuantity(productId: number) {
    const encodedUser = this.cookieService.get('user');
    if (encodedUser) {
      const user: User = this.encryptionService.decodeObject(
        encodedUser,
      ) as User;

      this.productService
        .increaseProductQuantity(productId, user.id)
        .subscribe({
          next: (response) => {
            console.log('Increase quantity success: ', response);
            this.toastr.success('Increase quantity success');
          },
          error: (error) => {
            console.error('Increase quantity error: ', error);
            this.toastr.error('Increase quantity error');
          },
        });
    }
  }

  handleRemoveProductFromCart(productId: number) {
    const encodedUser = this.cookieService.get('user');
    if (encodedUser) {
      const user: User = this.encryptionService.decodeObject(
        encodedUser,
      ) as User;

      this.productService.removeProductCart(productId, user.id).subscribe({
        next: (response) => {
          this.toastr.success('Remove product success');
          console.log('Remove product success: ', response);
        },
        error: (error) => {
          console.error('remove product error: ', error);
          this.toastr.error('remove product error');
        },
      });
    }
  }

  calculateTotalPrice(productCart: productCartResponse[]): number {
    let total: number = 0;
    this.productCart.forEach((item) => {
      total += item.product.price * item.quantity;
    });
    return total;
  }

  handleGetProductFromCart() {
    const encodedUser = this.cookieService.get('user');
    if (!encodedUser) {
      console.log('No information user');
      return;
    }
    const user: User = this.encryptionService.decodeObject(encodedUser) as User;
    this.productService.getCartByUserId(user.id).subscribe({
      next: (response) => {
        this.productCart = response.data;
        this.totalCart = response.data.length;
        this.totalPrice = this.calculateTotalPrice(this.productCart);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
