import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import {ApiClientService} from './api-client-service';
import {IAuthResponse, ILoginRequest, IRegisterRequest} from '../interfaces/auth.interface';
import {catchError, Observable, of, tap} from 'rxjs';
import {APIs} from '../constants/api.constants';
import {AppConstants} from '../constants/app.constants';
import {isPlatformBrowser} from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiClient = inject(ApiClientService);
  private platformId = inject(PLATFORM_ID);

  setLocalState() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(AppConstants.AUTH_KEY, 'true');
    }
  }

  clearLocalState() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(AppConstants.AUTH_KEY);
    }
  }

  getIsAuthenticated() {
    if(!isPlatformBrowser(this.platformId)) {
      return false;
    }
    return localStorage.getItem(AppConstants.AUTH_KEY) === 'true';
  }

  register(request: IRegisterRequest): Observable<IAuthResponse> {
    // Proactively clear any residual state before initiating registration
    this.clearLocalState();
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.REGISTER, request).pipe(
      tap(() => {
        this.setLocalState();
      })
    );
  }

  login(request: ILoginRequest): Observable<IAuthResponse> {
    // Proactively clear any residual state before initiating login
    this.clearLocalState();
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGIN, request).pipe(
      tap(() => {
        this.setLocalState();
      })
    );
  }

  logout(): Observable<any> {
    return this.apiClient.post<any>(APIs.AUTH.LOGOUT, {}).pipe(
      tap(() => {
        this.clearLocalState();
      }),
      catchError((err) => {
        this.clearLocalState();
        return of(null);
      })
    );
  }
}
