import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';

export interface HttpOptions {
  headers?: HttpHeaders | { [header: string]: string | string[] };
  params?: HttpParams | { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> };
  withCredentials?: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ApiClientService {
  constructor(private http: HttpClient) {}

  // Added options?: any to allow passing HTTP headers or credential flags
  get<T>(url: string, options?: HttpOptions) {
    return this.http.get<T>(url, options);
  }

  post<T>(url: string, body: unknown, options?: HttpOptions) {
    return this.http.post<T>(url, body, options);
  }

  put<T>(url: string, body: unknown, options?: HttpOptions) {
    return this.http.put<T>(url, body, options);
  }

  patch<T>(url: string, body: unknown, options?: HttpOptions) {
    return this.http.patch<T>(url, body, options);
  }

  delete<T>(url: string, options?: HttpOptions) {
    return this.http.delete<T>(url, options);
  }
}
