import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Page401Component } from './pages/page401/page401.component';
import { Page404Component } from './pages/page404/page404.component';
import { Page500Component } from './pages/page500/page500.component';

const routes: Routes = [
  {
    path: '404',
    component: Page404Component,
  },
  {
    path: '401',
    component: Page401Component,
  },
  {
    path: '500',
    component: Page500Component,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
