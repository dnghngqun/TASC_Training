import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductComponent } from '../component/product/product.component';
import { CatalogComponent } from './catalog/catalog.component';
import { HomeComponent } from './home.component';
import { SliderComponent } from './slider/slider.component';

@NgModule({
  imports: [FontAwesomeModule],
  declarations: [
    CatalogComponent,
    SliderComponent,
    HomeComponent,
    ProductComponent,
  ],
  exports: [HomeComponent],
})
export class HomeModule {}
