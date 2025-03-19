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
    // {
    //     // path: '',
    //     // pathMatch: 'full',
    //     // loadComponent: () => {
    //     //     return import('./components/quiz-card/quiz-card.component').then(
    //     //         module => module.QuizCardComponent
    //     //     )
    //     // }, 
    // }

];
