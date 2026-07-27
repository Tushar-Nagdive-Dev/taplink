import {computed, inject, Injectable, signal} from '@angular/core';
import {ApiClientService} from './api-client-service';
import {IAuthResponse, ILoginRequest, IRegisterRequest, ISessionResponse} from '../interfaces/auth.interface';
import {catchError, map, Observable, of, switchMap, tap} from 'rxjs';
import {APIs} from '../constants/api.constants';
import {isBrowser} from '../utils/platform.util';
import {PlatformService} from './platform.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiClient = inject(ApiClientService);
  private readonly platform = inject(PlatformService);
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
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGIN, request)
      .pipe(switchMap(response =>
          this.verifySession(true).pipe(map(() => response))));
  }

  logout(): Observable<IAuthResponse | null> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGOUT, {}).pipe(tap(() => {
      this.clearSession();
    }), catchError(() => {
      this.clearSession();
      return of(null);
    }));
  }

  verifySession(force = false): Observable<boolean> {
    if (this.platform.isServer) {
      return of(false);
    }
    if (!force && this.authState()) {
      return of(true);
    }
    return this.apiClient
      .get<ISessionResponse>(APIs.AUTH.USER_SESSION)
      .pipe(
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
