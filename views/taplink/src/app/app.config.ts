import {
  ApplicationConfig, inject, provideAppInitializer,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import {provideHttpClient, withFetch, withInterceptors} from '@angular/common/http';
import {authInterceptor} from './interceptors/auth.interceptor';
import {errorInterceptor} from './interceptors/error.interceptor';
import {AuthService} from './services/auth-service';
import {firstValueFrom} from 'rxjs';
import {PlatformService} from './services/platform.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes), provideClientHydration(withEventReplay()),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor, errorInterceptor])),
    provideAppInitializer(() => {
      const platform = inject(PlatformService);
      if (platform.isServer) {
        return Promise.resolve();
      }
      const auth = inject(AuthService);
      return firstValueFrom(auth.verifySession()).catch(() => false);
    })
  ]
};
