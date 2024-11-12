import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  faBagShopping,
  faCaretDown,
  faHeart,
  faMagnifyingGlass,
  faUser,
} from '@fortawesome/free-solid-svg-icons';

import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { ProductService } from '../../services/product.service';
import { CookieService } from 'ngx-cookie-service';
import { EncryptionService } from '../../services/encryption.service';
import { User } from '../../models/user.model';
import { Product } from '../../models/product.model';
import { productCartResponse } from '../../dto/productCartResponse.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
    private productService:ProductService,
    private encryptionService:EncryptionService,
    private cookieService:CookieService
  ) {}
  quantity :number = 1;
  totalCart:number = 0;
  productCart: productCartResponse[] = [];
  isLoggedIn: boolean = false;
  totalPriceProduct: number = 0;
  private intervalId: any;
  ngOnInit() {
    this.updateLoginStatus();
    this.intervalId = setInterval(() => {
      this.updateLoginStatus();
    }, 2000);

  }
  faCaretDown = faCaretDown;
  faMagnifyingGlass = faMagnifyingGlass;
  faUser = faUser;
  faHeart = faHeart;
  faBagShopping = faBagShopping;

  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }
  private updateLoginStatus() {
    this.isLoggedIn = this.authService.getIsLoggedIn();
    if(this.isLoggedIn){
      this.handleGetProductFromCart();
    }
    console.log('isLoggedIn: ', this.isLoggedIn);
  }



  handleDecreaseQuantity(productId:number) {
    const encodedUser = this.cookieService.get('user');
      if (encodedUser) {
        const user: User = this.encryptionService.decodeObject(
          encodedUser,
        ) as User;

        this.productService
          .decreaseProductQuantity(productId, user.userId)
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

  handleIncreaseQuantity(productId:number) {
    const encodedUser = this.cookieService.get('user');
      if (encodedUser) {
        const user: User = this.encryptionService.decodeObject(
          encodedUser,
        ) as User;

        this.productService
          .increaseProductQuantity(productId, user.userId)
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
  handleRemoveProductFromCart(productId:number) {
    const encodedUser = this.cookieService.get('user');
      if (encodedUser) {
        const user: User = this.encryptionService.decodeObject(
          encodedUser,
        ) as User;

        this.productService
          .removeProductCart(productId, user.userId)
          .subscribe({
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

  calculateTotalPrice(productCart:productCartResponse[]):number{
      let total:number = 0;
    this.productCart.forEach((item) =>{
      total += item.product.price * item.quantity;
    })
    return total;
  }

  handleLogout() {
    // this.toastr.info("Starting logout...");
    this.authService.logout().then((success) => {
      if (success) {
        // this.showSuccess('Logout success 👋');
        console.log('Logout success 👋');
        this.toastr.success('Logout success 👋');
        this.router.navigate(['login']);
      } else {
        // this.showError('Logout error 🧐');
        console.log('Logout error 🧐');
        this.toastr.error('Logout error 🧐');
      }
    });
  }
  handleGetProductFromCart(){
    const encodedUser = this.cookieService.get('user');
    if(!encodedUser){
      console.log('No information user');
      return;
    }
    const user: User = this.encryptionService.decodeObject(encodedUser) as User;
    this.productService.getCartByUserId(user.userId).subscribe({
      next:(response) =>{
        this.productCart = response.data;
        this.totalCart = response.data.length;
        this.totalPriceProduct = this.calculateTotalPrice(this.productCart);
      },
      error:(error) =>{
        console.error(error);
      }
    })
  }

  getUserInformation(){

  }
}
