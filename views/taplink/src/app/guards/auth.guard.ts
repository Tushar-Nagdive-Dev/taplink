import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { map, catchError, of } from 'rxjs';

import { AuthService } from '../services/auth-service';

export const authGuard: CanActivateFn = () => {

  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.verifySession().pipe(

    map(authenticated =>
      authenticated
        ? true
        : router.createUrlTree(['/signin'])
    ),

    catchError(() =>
      of(router.createUrlTree(['/signin']))
    )

  );

};
