import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ILink, ILinkRequest } from '../interfaces/link.interface';
import { ApiClientService } from './api-client-service';
import {APIs} from '../constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class LinkService {
  private apiClient = inject(ApiClientService);

  getAllLinks(): Observable<ILink[]> {
    return this.apiClient.get<ILink[]>(APIs.USER_LINKS.LINKS);
  }

  createLink(request: ILinkRequest): Observable<ILink> {
    return this.apiClient.post<ILink>(APIs.USER_LINKS.LINKS, request);
  }

  updateLink(id: number, request: ILinkRequest): Observable<ILink> {
    return this.apiClient.put<ILink>(`${APIs.USER_LINKS.LINKS}/${id}`, request);
  }

  deleteLink(id: number): Observable<void> {
    return this.apiClient.delete<void>(`${APIs.USER_LINKS.LINKS}/${id}`);
  }
}
