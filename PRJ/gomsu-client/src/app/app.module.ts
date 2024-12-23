import { provideHttpClient } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgxPaginationModule } from 'ngx-pagination';

import { ToastrModule } from 'ngx-toastr';
import { environment } from '../environments/environment.development';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CartModule } from './pages/cart/cart.module';
import { FooterComponent } from './pages/footer/footer.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { HomeModule } from './pages/home/home.module';
import { LoginComponent } from './pages/login/login.component';
import { NavbarComponent } from './pages/navbar/navbar.component';
import { ProductAllComponent } from './pages/product-all/product-all.component';
import { RegisterComponent } from './pages/register/register.component';
import { BreadcrumbModule } from './shared/breadcrumb/breadcrumb.module';
import { ProductModule } from './shared/product/product.module';
import { PaymentComponent } from './pages/payment/payment.component';
import { AccountComponent } from './pages/account/account.component';
import { SidebarAccountComponent } from './pages/account/sidebar-account/sidebar-account.component';
import { InfomationComponent } from './pages/account/infomation/infomation.component';
import { OrderComponent } from './pages/account/order/order.component';
import { ChangePasswordComponent } from './pages/account/change-password/change-password.component';
import { AddressComponent } from './pages/account/address/address.component';

const googleClientId = environment.googleClientId;
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    ProductAllComponent,
    RegisterComponent,
    LoginComponent,
    ForgotPasswordComponent,
    PaymentComponent,
    AccountComponent,
    SidebarAccountComponent,
    InfomationComponent,
    OrderComponent,
    ChangePasswordComponent,
    AddressComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    FormsModule,
    CartModule,
    HomeModule,
    BreadcrumbModule,
    ProductModule,
    RouterModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 1500,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      disableTimeOut: false,
      tapToDismiss: true,
      progressBar: true,
      autoDismiss: true,
    }),
    NgxPaginationModule,
  ],

  providers: [provideHttpClient()],
  bootstrap: [AppComponent],
})
export class AppModule {}
