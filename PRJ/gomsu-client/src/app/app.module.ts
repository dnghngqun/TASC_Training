import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgxPaginationModule } from 'ngx-pagination';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { ToastrModule } from 'ngx-toastr';
import { environment } from '../environments/environment.development';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './pages/footer/footer.component';
import { HomeModule } from './pages/home/home.module';
import { LoginComponent } from './pages/login/login.component';
import { NavbarComponent } from './pages/navbar/navbar.component';
import { ProductAllComponent } from './pages/product-all/product-all.component';
import { RegisterComponent } from './pages/register/register.component';
import { BreadcrumbModule } from './shared/breadcrumb/breadcrumb.module';
import { ProductModule } from './shared/product/product.module';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';

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

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    FormsModule,
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

  providers: [
    provideHttpClient(),
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
