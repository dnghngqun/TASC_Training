import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { HomeComponent } from './home.component';
import { FooterComponent } from '../../shared/footer/footer.component';

@NgModule({
  declarations: [HomeComponent, FooterComponent],
  imports: [CommonModule],
  exports: [HomeComponent],
})
export class HomeModule {}
