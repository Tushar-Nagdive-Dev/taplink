import { Routes } from '@angular/router';
import { TaplinkView } from './components/taplink-view/taplink-view';
import { SignIn } from './components/auth/sign-in/sign-in';
import { SignUp } from './components/auth/sign-up/sign-up';
import {TaplinkDashboard} from './components/taplink-dashboard/taplink-dashboard';
import {AuthErrorComponent} from './components/auth/auth-error.component';
import {authGuard} from './guards/auth.guard';
import {LinkManager} from './components/link-manager/link-manager';
import {QrCode} from './components/qr-code/qr-code';

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
        canActivateChild: [authGuard],
        children: [
          { path: 'links', component: LinkManager },
          { path: 'qr', component: QrCode },
          { path: '', redirectTo: 'links', pathMatch: 'full'}
        ]
    },
    { path: '**', redirectTo: '' } // Fallback catch-all route
];
