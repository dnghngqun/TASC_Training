import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductComponent } from './product.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [ProductComponent],
  imports: [CommonModule, FontAwesomeModule, RouterModule],
  exports: [ProductComponent],
})
export class ProductModule {}
