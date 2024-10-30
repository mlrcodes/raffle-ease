import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: '', redirectTo: 'client', pathMatch: 'full'},
    { 
        path: 'client', 
        loadChildren: () => import("./modules/client/client-routing.module").then(m => m.ClientRoutingModule) 
    },
    { 
        path: 'admin', 
        loadChildren: () => import("./modules/admin/admin-routing.module").then(m => m.AdminRoutingModule) 
    },
];
