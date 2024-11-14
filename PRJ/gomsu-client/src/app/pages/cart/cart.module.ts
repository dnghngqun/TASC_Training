import { NgModule } from '@angular/core';
import { CartComponent } from './cart.component';
import { BreadcrumbModule } from '../../shared/breadcrumb/breadcrumb.module';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  imports: [BreadcrumbModule, CommonModule, BrowserModule, FormsModule, FontAwesomeModule],
  declarations: [ CartComponent],
  exports: [CartComponent],
})
export class CartModule {}
