import { Routes } from '@angular/router';
import { TaplinkView } from './components/taplink-view/taplink-view';
import { SignIn } from './components/auth/sign-in/sign-in';
import { SignUp } from './components/auth/sign-up/sign-up';

export const routes: Routes = [
    {
        path: '',
        component: TaplinkView,
        children: [
            {
                path: 'signin',
                component: SignIn
            }, 
            {
                path: 'signup',
                component: SignUp
            },
            { 
                // If someone visits the root URL (tap.link/), automatically redirect them to the sign-in form
                path: '', 
                redirectTo: 'signin', 
                pathMatch: 'full' 
            }
        ]
    }
];
