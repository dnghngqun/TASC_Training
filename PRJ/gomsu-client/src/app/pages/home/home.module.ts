import { NgModule } from '@angular/core';
import { CatalogComponent } from './catalog/catalog.component';
import { HomeComponent } from './home.component';
import { SliderComponent } from './slider/slider.component';

@NgModule({
  imports: [],
  declarations: [CatalogComponent, SliderComponent, HomeComponent],
  exports: [HomeComponent],
})
export class HomeModule {}
