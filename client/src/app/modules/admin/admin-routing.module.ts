import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RafflesResolver } from '../../core/resolvers/raffles.resolver';
import { AuthGuard } from '../../core/guards/auth.guard';

const routes: Routes = [
  {
    path: '', loadChildren: () => import("./components/pages/admin-layout/admin-layout.routes").then(c => c.ADMIN_ROUTES),
    canActivate: [AuthGuard],
    resolve: {
      raffles: RafflesResolver
    }
  },
  {
    path: 'auth', loadChildren: () => import('./components/pages/auth/auth.routes').then(c => c.AUTH_ROUTES)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
