import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Toast, ToastType} from '../modals/app.modal';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private toastSubject = new BehaviorSubject<Toast[]>([]);
  toasts$ = this.toastSubject.asObservable();

  show(message: string, type: ToastType = 'info'): void {
    const id = Math.random().toString(36).substring(2, 9);
    const newToast : Toast = {id, message, type};

    const currentToasts = this.toastSubject.value;
    this.toastSubject.next([newToast, ...currentToasts]);

    setTimeout(() => {
      this.remove(id);
    }, 3500);
  }

  remove(id: string) {
    const currentToasts = this.toastSubject.value;
    this.toastSubject.next(currentToasts.filter(toast => toast.id !== id));
  }
}
