import {Component, inject} from '@angular/core';
import {ToastService} from '../../services/toast-service';
import {CommonModule} from '@angular/common';
import {AlertCircle, AlertTriangle, CheckCircle2, Info, LucideAngularModule, X} from 'lucide-angular';

@Component({
  selector: 'app-toast',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './toast.html',
  styleUrl: './toast.scss',
})
export class Toast {
  toastService = inject(ToastService);

  readonly CheckIcon = CheckCircle2;
  readonly ErrorIcon = AlertCircle;
  readonly InfoIcon = Info;
  readonly WarningIcon = AlertTriangle;
  readonly CloseIcon = X;
}
