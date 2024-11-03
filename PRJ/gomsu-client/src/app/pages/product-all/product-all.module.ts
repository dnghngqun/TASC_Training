import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { BreadcrumbModule } from '../../shared/breadcrumb/breadcrumb.module';
import { ProductModule } from '../../shared/product/product.module';
import { ProductAllComponent } from './product-all.component';

@NgModule({
  declarations: [ProductAllComponent],
  imports: [CommonModule, ProductModule, BreadcrumbModule],
  exports: [ProductAllComponent],
})
export class ProductAllModule {}
