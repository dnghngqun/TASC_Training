import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  constructor(
    private toastr: ToastrService,
    private authService: AuthService,
    private router: Router,
  ) {}
  isLoggedIn: boolean = false;
  private intervalId: any;

  ngOnInit() {
    this.updateLoginStatus();
    this.intervalId = setInterval(() => {
      this.updateLoginStatus();
    }, 1000);
  }
  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }
  private updateLoginStatus() {
    this.isLoggedIn = this.authService.getIsLoggedIn();
    console.log('isLoggedIn: ', this.isLoggedIn);
    if (!this.isLoggedIn) {
      window.location.href = 'http://localhost:4200/login';
    }
  }

  handleLogout() {
    // this.toastr.info("Starting logout...");
    this.authService.logout().then((success) => {
      if (success) {
        // this.showSuccess('Logout success 👋');
        console.log('Logout success 👋');
        this.toastr.success('Logout success 👋');
        window.location.href = 'http://localhost:4200/login';
      } else {
        // this.showError('Logout error 🧐');
        console.log('Logout error 🧐');
        this.toastr.error('Logout error 🧐');
      }
    });
  }
}
