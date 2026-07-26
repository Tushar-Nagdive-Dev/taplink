import {inject, Injectable} from '@angular/core';
import {ApiClientService} from './api-client-service';
import {Observable} from 'rxjs';
import {IUserProfile} from '../interfaces/user.interface';
import {APIs} from '../constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  apiClient = inject(ApiClientService);

  getUserProfile(): Observable<IUserProfile> {
    return this.apiClient.get(APIs.USER.USER_PROFILE);
  }

  updateUserProfile(userProfile: IUserProfile): Observable<IUserProfile> {
    return this.apiClient.put(APIs.USER.USER_PROFILE, userProfile);
  }
}
