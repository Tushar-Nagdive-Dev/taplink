import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import {PlatformService} from '../services/platform.service';
import {AuthService} from '../services/auth-service';

export const authGuard: CanActivateFn = () => {

  const platform = inject(PlatformService);

  if (platform.isServer) {
    return true;
  }

  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated()) {
    return true;
  }

  return auth.verifySession().pipe(
    map(ok =>
      ok
        ? true
        : router.parseUrl('/signin')
    )
  );

};
