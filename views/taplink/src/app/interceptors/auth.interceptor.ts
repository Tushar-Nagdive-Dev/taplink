import {HttpInterceptorFn} from '@angular/common/http';
import {environment} from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const isApiUrl = request.url.startsWith(environment.apiBaseUrl);

  if (isApiUrl) {
    const clonedRequest = request.clone({
      withCredentials: true
    });

    return next(clonedRequest);
  }

  return next(request);
};
