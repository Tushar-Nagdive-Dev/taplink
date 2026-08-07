import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {ILink, ILinkRequest, IReorderLink} from '../interfaces/link.interface';
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
    return this.apiClient.put<ILink>(APIs.USER_LINKS.BY_ID(id), request);
  }

  deleteLink(id: number): Observable<void> {
    return this.apiClient.delete<void>(APIs.USER_LINKS.BY_ID(id));
  }

  patchFavorite(id: number, isFavorite: boolean): Observable<ILink> {
    return this.apiClient.patch<ILink>(APIs.USER_LINKS.FAVORITE_PATCHED(id, isFavorite), {});
  }

  patchStatus(id: number, isActive: boolean): Observable<boolean> {
    return this.apiClient.patch<boolean>(APIs.USER_LINKS.UPDATE_STATUS(id, isActive), {});
  }

  patchReorderLink(linkPositions: IReorderLink[]): Observable<void> {
    return this.apiClient.patch<void>(APIs.USER_LINKS.LINKS_REORDER, {linkPositions});
  }

  // --- QR Code Methods Handled via Service ---
  getQrCodeImage(id: number): Observable<Blob> {
    return this.apiClient.get(APIs.USER_LINKS.GET_QR_CODE(id), { responseType: 'blob' });
  }

  updateQrConfig(id: number, payload: any): Observable<any> {
    return this.apiClient.put<any>(APIs.USER_LINKS.GET_QR_CODE(id), payload);
  }

}
