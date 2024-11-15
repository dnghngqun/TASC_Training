import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CartComponent} from './pages/cart/cart.component';
import {ForgotPasswordComponent} from './pages/forgot-password/forgot-password.component';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/login/login.component';
import {ProductAllComponent} from './pages/product-all/product-all.component';
import {RegisterComponent} from './pages/register/register.component';
import {PaymentComponent} from './pages/payment/payment.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'collections/all', component: ProductAllComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'cart', component: CartComponent},
  {path: 'payment', component: PaymentComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'}),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
