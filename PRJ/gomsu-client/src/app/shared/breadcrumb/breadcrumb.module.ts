import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { BreadcrumbComponent } from './breadcrumb.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [BreadcrumbComponent],
  imports: [CommonModule, FontAwesomeModule, RouterModule],
  exports: [BreadcrumbComponent],
})
export class BreadcrumbModule {}
