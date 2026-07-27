import {computed, inject, Injectable, signal} from '@angular/core';
import {ApiClientService} from './api-client-service';
import {IAuthResponse, ILoginRequest, IRegisterRequest, ISessionResponse} from '../interfaces/auth.interface';
import {catchError, map, Observable, of, tap} from 'rxjs';
import {APIs} from '../constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiClient = inject(ApiClientService);
  /**
   * Current authenticated session.
   * null = not authenticated.
   */
  private readonly authState = signal<ISessionResponse | null>(null);
  /**
   * Read-only session.
   */
  readonly session = this.authState.asReadonly();
  /**
   * True if user is authenticated.
   */
  readonly isAuthenticated = computed(() => this.authState() !== null);

  register(request: IRegisterRequest): Observable<IAuthResponse> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.REGISTER, request);
  }

  login(request: ILoginRequest): Observable<IAuthResponse> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGIN, request);
  }

  logout(): Observable<IAuthResponse | null> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGOUT, {}).pipe(tap(() => {
      this.clearSession();
    }), catchError(() => {
      this.clearSession();
      return of(null);
    }));
  }

  verifySession(): Observable<boolean> {
    return this.apiClient.get<ISessionResponse>(APIs.AUTH.USER_SESSION).pipe(
      tap(session => this.authState.set(session)),
      map(() => true),
      catchError(() => {
        this.clearSession();
        return of(false);
      })
    );
  }

  /**
   * Clears client-side authentication state.
   */
  clearSession(): void {
    this.authState.set(null);
  }

}
