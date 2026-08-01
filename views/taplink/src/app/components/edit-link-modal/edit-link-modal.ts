import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog'; // <-- Angular CDK Dialog imports
import { ILink, ILinkRequest } from '../../interfaces/link.interface';
import { LinkService } from '../../services/link-service';
import { ToastService } from '../../services/toast-service';
import { AppConstants } from '../../constants/app.constants';
import { HasValueUtils } from '../../utils/has-value.utils';
import {
  LucideAngularModule, Link2, Type, X, Tag, Calendar,
  Palette, ExternalLink, Settings
} from 'lucide-angular';
import {HasValuePipe} from '../../pipes/has-value-pipe';

@Component({
  selector: 'app-edit-link-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule, HasValuePipe],
  templateUrl: './edit-link-modal.html',
  styleUrl: './edit-link-modal.scss',
})
export class EditLinkModal implements OnInit {
  // 1. Inject DialogRef to control closing and returning updated data
  private dialogRef:DialogRef<ILink> = inject<DialogRef<ILink>>(DialogRef);

  // 2. Inject DIALOG_DATA to receive the selected ILink from LinkManager
  public link: ILink = inject<ILink>(DIALOG_DATA);

  private linkService = inject(LinkService);
  private toastService = inject(ToastService);

  // --- Icons ---
  readonly CloseIcon = X;
  readonly LinkIcon = Link2;
  readonly TypeIcon = Type;
  readonly TagIcon = Tag;
  readonly CalendarIcon = Calendar;
  readonly PaletteIcon = Palette;
  readonly ExternalIcon = ExternalLink;
  readonly SettingsIcon = Settings;

  isLoading = false;

  // Form state initialized with all available backend fields
  editData: ILinkRequest = {
    title: '',
    url: '',
    isActive: true
  };

  ngOnInit(): void {
    // Populate the form with the injected link data immediately when opened
    if (this.link) {
      this.editData = {
        title: this.link.title || '',
        url: this.link.url || '',
        isActive: this.link.isActive ?? true,
        label: this.link.label || '',
        colorCode: this.link.colorCode || '#FFFFFF',
        customSlug: this.link.customSlug || '',
        expiresAt: this.link.expiresAt || '',
        isFavorite: this.link.isFavorite ?? false
      };
    }
  }

  saveChanges() {
    if (!HasValueUtils.hasValue(this.editData.title) || !HasValueUtils.hasValue(this.editData.url)) {
      this.toastService.show(AppConstants.TOAST_MESSAGES.FILL_TITLE_URL, AppConstants.TOAST_TYPE.WARNING);
      return;
    }

    this.isLoading = true;

    // Call your Spring Boot update endpoint
    this.linkService.updateLink(this.link.id, this.editData).subscribe({
      next: (updatedLink: ILink) => {
        this.isLoading = false;
        this.toastService.show('Link configuration saved!', AppConstants.TOAST_TYPE.SUCCESS);

        // Return the fresh LinkResponse object back to the parent component
        this.dialogRef.close(updatedLink);
      },
      error: () => {
        this.isLoading = false;
        this.toastService.show('Failed to update link.', AppConstants.TOAST_TYPE.ERROR);
      }
    });
  }

  closeModal() {
    // Close without returning any data
    this.dialogRef.close();
  }
}
