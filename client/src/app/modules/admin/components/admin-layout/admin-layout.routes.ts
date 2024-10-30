import { Routes } from "@angular/router";
import { PanelComponent } from "../pages/panel/panel.component";
import { RaffleManagementComponent } from "../pages/raffle-management/raffle-management.component";
import { RaffleCreationComponent } from "../pages/raffle-creation/raffle-creation.component";
import { RaffleEditionComponent } from "../pages/raffle-edition/raffle-edition.component";

export const ADMIN_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('./admin-layout.component').then(c => c.AdminLayoutComponent),
        children: [
            { path: '', redirectTo: 'panel', pathMatch: 'full' },    
            { 
                path: 'panel',
                loadComponent: () => import('../pages/panel/panel.component').then(c => c.PanelComponent)
            },
            { 
                path: 'management',
                loadComponent: () => import('../pages/raffle-management/raffle-management.component').then(c => c.RaffleManagementComponent)
            },
            { 
                path: 'create',
                loadComponent: () => import('../pages/raffle-creation/raffle-creation.component').then(c => c.RaffleCreationComponent)
            },
            { 
                path: 'edit',
                loadComponent: () => import('../pages/raffle-edition/raffle-edition.component').then(c => c.RaffleEditionComponent)
            },
        ]
    }
];