import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogRef } from '@angular/cdk/dialog'; // <-- Angular CDK Dialog
import { ILink, ILinkRequest } from '../../interfaces/link.interface';
import { LinkService } from '../../services/link-service';
import { ToastService } from '../../services/toast-service';
import { Link2, X, Type, LucideAngularModule } from 'lucide-angular';
import { HasValueUtils } from '../../utils/has-value.utils';
import { AppConstants } from '../../constants/app.constants';

@Component({
  selector: 'app-add-link-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './add-link-modal.html',
  styleUrl: './add-link-modal.scss',
})
export class AddLinkModal implements OnInit {
  // Inject DialogRef to control closing and returning data
  private dialogRef = inject<DialogRef<ILink>>(DialogRef);
  private linkService = inject(LinkService);
  private toastService = inject(ToastService);

  readonly CloseIcon = X;
  readonly LinkIcon = Link2;
  readonly TypeIcon = Type;

  isLoading: boolean = false;

  newLink: ILinkRequest = {
    title: '',
    url: '',
    isActive: true
  };

  ngOnInit(): void {}

  saveLink() {
    if (!HasValueUtils.hasValue(this.newLink.title) || !HasValueUtils.hasValue(this.newLink.url)) {
      this.toastService.show(AppConstants.TOAST_MESSAGES.FILL_TITLE_URL, AppConstants.TOAST_TYPE.WARNING);
      return;
    }

    this.isLoading = true;
    this.linkService.createLink(this.newLink).subscribe({
      next: (savedLink: ILink) => {
        this.isLoading = false;
        this.toastService.show(AppConstants.TOAST_MESSAGES.LINK_ADDED_SUCCESSFULLY, AppConstants.TOAST_TYPE.SUCCESS);

        // Close the CDK Dialog and pass the newly created link back to LinkManager!
        this.dialogRef.close(savedLink);
      },
      error: () => {
        this.isLoading = false;
        this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_CREATE_LINK, AppConstants.TOAST_TYPE.ERROR);
      }
    });
  }

  resetAndClose() {
    // Close without returning data
    this.dialogRef.close();
  }
}
