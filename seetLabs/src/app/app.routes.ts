import { Routes } from '@angular/router';

export const routes: Routes = [{
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
        path: 'json_server_test/:id/:assignmentNumber',
        pathMatch: 'full',
        loadComponent: () => {
            return import('./pages/json-server-test/json-server-test.component').then(
                module => module.JsonServerTestComponent
            )
        }, 
    }

];