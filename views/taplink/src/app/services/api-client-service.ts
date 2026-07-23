import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiClientService {
  constructor(private http: HttpClient) {}

  // Added options?: any to allow passing HTTP headers or credential flags
  get<T>(url: string, options?: any) {
    return this.http.get<T>(url, options);
  }

  post<T>(url: string, body: unknown, options?: any) {
    return this.http.post<T>(url, body, options);
  }

  put<T>(url: string, body: unknown, options?: any) {
    return this.http.put<T>(url, body, options);
  }

  patch<T>(url: string, body: unknown, options?: any) {
    return this.http.patch<T>(url, body, options);
  }

  delete<T>(url: string, options?: any) {
    return this.http.delete<T>(url, options);
  }
}
