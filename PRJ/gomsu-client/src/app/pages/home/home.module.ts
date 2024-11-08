import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductModule } from '../../shared/product/product.module';
import { CatalogComponent } from './catalog/catalog.component';
import { HomeComponent } from './home.component';
import { SliderComponent } from './slider/slider.component';

@NgModule({
  imports: [FontAwesomeModule, ProductModule],
  declarations: [CatalogComponent, SliderComponent, HomeComponent],
  exports: [HomeComponent, CatalogComponent, SliderComponent],
})
export class HomeModule {}
