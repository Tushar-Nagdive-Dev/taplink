import {inject, Injectable} from '@angular/core';
import {ApiClientService} from './api-client-service';
import {IAuthResponse, ILoginRequest, IRegisterRequest} from '../interfaces/auth.interface';
import {Observable} from 'rxjs';
import {APIs} from '../constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiClient = inject(ApiClientService);

  register(request: IRegisterRequest): Observable<IAuthResponse> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.REGISTER, request, {
      withCredentials: true
    });
  }

  login(request: ILoginRequest): Observable<IAuthResponse> {
    return this.apiClient.post<IAuthResponse>(APIs.AUTH.LOGIN, request, {
      withCredentials: true
    })
  }

  logout(): Observable<any> {
    return this.apiClient.post<any>(APIs.AUTH.LOGOUT, {}, {
      withCredentials: true
    });
  }
}
