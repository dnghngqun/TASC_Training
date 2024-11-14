import { NgModule } from '@angular/core';
import { CartComponent } from './cart.component';
import { BreadcrumbModule } from '../../shared/breadcrumb/breadcrumb.module';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [BreadcrumbModule, CommonModule, BrowserModule, FormsModule],
  declarations: [ CartComponent],
  exports: [CartComponent],
})
export class CartModule {}
