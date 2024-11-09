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
    private productService:ProductService
  ) {}
  quantity :number = 1;
  totalCart:number = 0;
  isLoggedIn: boolean = false;
  private intervalId: any;
  ngOnInit() {
    this.updateLoginStatus();
    this.intervalId = setInterval(() => {
      this.updateLoginStatus();
    }, 1000);
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
    console.log('isLoggedIn: ', this.isLoggedIn);
  }


  handleDecreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  handleIncreaseQuantity() {
    this.quantity++;
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
 }
}
