import {inject, Injectable} from '@angular/core';
import {ApiClientService} from './api-client-service';
import {IAuthResponse, ILoginRequest, IRegisterRequest} from '../interfaces/auth.interface';
import {Observable} from 'rxjs';
import {APIs} from '../constants/api.constants';
import {AppConstants} from '../constants/app.constants';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiClient = inject(ApiClientService);

  setLocalState() {
    localStorage.setItem(AppConstants.AUTH_KEY, 'true');
  }

  clearLocalState() {
    localStorage.removeItem(AppConstants.AUTH_KEY);
  }

  getIsAuthenticated() {
    return localStorage.getItem(AppConstants.AUTH_KEY) === 'true';
  }

  register(request: IRegisterRequest): Observable<IAuthResponse> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.REGISTER, request);
  }

  login(request: ILoginRequest): Observable<IAuthResponse> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGIN, request)
  }

  logout(): Observable<any> {
    return this.apiClient.post<any>(APIs.AUTH.LOGOUT, {});
  }
}
