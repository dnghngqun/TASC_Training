import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductModule } from '../../shared/product/product.module';
import { CatalogComponent } from './catalog/catalog.component';
import { HomeComponent } from './home.component';
import { SliderComponent } from './slider/slider.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [FontAwesomeModule, ProductModule, CommonModule, RouterModule],
  declarations: [CatalogComponent, SliderComponent, HomeComponent],
  exports: [HomeComponent, CatalogComponent, SliderComponent],
})
export class HomeModule {}
