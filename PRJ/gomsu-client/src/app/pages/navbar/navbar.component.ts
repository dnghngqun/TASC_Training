import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  faBagShopping,
  faCaretDown,
  faHeart,
  faMagnifyingGlass,
  faUser,
} from '@fortawesome/free-solid-svg-icons';

import { LoginService } from '../../services/login_service.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  constructor(
    private loginService: LoginService,

  ) {}
  isLoggedIn: boolean = false;
  private intervalId: any;
  ngOnInit() {
    this.updateLoginStatus();
    this.intervalId = setInterval(() => {
      this.updateLoginStatus();
    }, 5000);
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
    this.isLoggedIn = this.loginService.getIsLoggedIn();
    console.log('isLoggedIn: ', this.isLoggedIn);
  }


  handleLogout() {
    this.loginService.logout().then((success) => {
      if (success) {
        // this.showSuccess('Logout success 👋');
        console.log("Logout success 👋");
      } else {
        // this.showError('Logout error 🧐');
        console.log("Logout error 🧐")
      }
    });
  }
}
