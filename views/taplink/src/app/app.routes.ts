import { Routes } from '@angular/router';
import { TaplinkView } from './components/taplink-view/taplink-view';
import { SignIn } from './components/auth/sign-in/sign-in';
import { SignUp } from './components/auth/sign-up/sign-up';
import {TaplinkDashboard} from './components/taplink-dashboard/taplink-dashboard';
import {AuthErrorComponent} from './components/auth/auth-error.component';
import {authGuard} from './guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        component: TaplinkView,
        children: [
            { path: 'signin', component: SignIn},
            { path: 'signup', component: SignUp},
            { path: '', redirectTo: 'signin', pathMatch: 'full'}
        ]
    },
    {
        path: 'auth-error',
        component: AuthErrorComponent
    },
    {
        path: 'taplink-dashboard',
        component: TaplinkDashboard,
        canActivate: [authGuard],
        children: []
    },
    { path: '**', redirectTo: '' } // Fallback catch-all route
];
