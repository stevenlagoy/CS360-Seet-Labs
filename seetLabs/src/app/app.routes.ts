import { ActivatedRoute, Routes } from '@angular/router';
import { JsonServerTestService } from './services/json-server-test.service';
import { inject } from '@angular/core';
import { catchError } from 'rxjs';

export const routes: Routes = [
    
    {
        path: '',
        pathMatch: 'full',
        loadComponent: () => {
            return import('./pages/home-page/home-page.component').then(
                module => module.HomePageComponent
            )
        },
    },
    
    
    {
        path: 'components',
        pathMatch: 'full',
        loadComponent: () => {
            return import('./pages/components/components.component').then(
                module => module.ComponentsComponent
            )
        },
    },

    {
        path: 'playground',
        pathMatch: 'full',
        loadComponent: () => {
            return import('./pages/playground/playground.component').then(
                module => module.PlaygroundComponent
            )
        },
    },

    {
        path: 'module/:id/:assignmentNumber',
        pathMatch: 'full',
        loadComponent: () => {
            return import('./pages/page-router-component/page-router-component.component').then(
                module => module.PageRouterComponentComponent
            )
        }, 
    }

];