import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductComponent } from './product.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [ProductComponent],
  imports: [CommonModule, FontAwesomeModule, RouterModule, FormsModule],
  exports: [ProductComponent],
})
export class ProductModule {}
