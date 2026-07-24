export type ToastType = 'success' | 'error' | 'info' | 'warning' | string;

export interface Toast {
  id: string;
  message: string;
  type: ToastType;
}
