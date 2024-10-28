import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './pages/navbar/navbar.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import { SliderComponent } from './pages/home/slider/slider.component';
import { CatalogComponent } from './pages/home/catalog/catalog.component';
import { HomeComponent } from './pages/home/home.component';
@NgModule({
  declarations: [AppComponent, NavbarComponent, SliderComponent, CatalogComponent, HomeComponent],
  imports: [BrowserModule, AppRoutingModule, FontAwesomeModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
