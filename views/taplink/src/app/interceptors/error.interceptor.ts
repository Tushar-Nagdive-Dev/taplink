import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { APIs } from '../constants/api.constants';
import { AuthService } from '../services/auth-service';
import { PlatformService } from '../services/platform.service';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {

  const router = inject(Router);
  const auth = inject(AuthService);
  const platform = inject(PlatformService);

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      // Ignore SSR completely.
      if (platform.isServer) {
        return throwError(() => error);
      }
      if (error.status === 401) {
        // Ignore the initial session restore request.
        if (request.url.endsWith(APIs.AUTH.USER_SESSION)) {
          return throwError(() => error);
        }
        // If we never had a session, don't redirect.
        if (!auth.isAuthenticated()) {
          return throwError(() => error);
        }
        // Session expired while user was logged in.
        auth.clearSession();
        router.navigate(['/auth-error'], {
          queryParams: {
            reason: 'session_expired'
          }
        });
      }
      return throwError(() => error);
    })
  );
};
