import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { APIs } from '../constants/api.constants';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {

  const router = inject(Router);

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {

      if (error.status === 401) {

        // Ignore the authentication probe.
        if (request.url.endsWith(APIs.AUTH.USER_SESSION)) {
          return throwError(() => error);
        }

        // Only redirect if an already authenticated user loses the session.
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
