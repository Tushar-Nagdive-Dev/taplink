import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LinkService } from '../../services/link-service';
import { ToastService } from '../../services/toast-service';
import { ILink, ILinkRequest } from '../../interfaces/link.interface';
import { AppConstants } from '../../constants/app.constants';
import {
  LucideAngularModule, Link2, Type, X, Tag, Calendar,
  Palette, ExternalLink, Settings
} from 'lucide-angular';

@Component({
  selector: 'app-edit-link-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './edit-link-modal.html'
})
export class EditLinkModal implements OnChanges {
  @Input() isOpen: boolean = false;
  @Input() link: ILink | null = null;

  @Output() close = new EventEmitter<void>();
  @Output() linkUpdated = new EventEmitter<ILink>();

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

  // The local form state
  editData: ILinkRequest = {
    title: '',
    url: '',
    isActive: true
  };

  // When the modal opens and receives a link, copy its data into our form!
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isOpen']?.currentValue === true && this.link) {
      this.editData = {
        title: this.link.title,
        url: this.link.url,
        isActive: this.link.isActive,
        label: this.link.label || '',
        colorCode: this.link.colorCode || '#FFFFFF',
        customSlug: this.link.customSlug || '',
        expiresAt: this.link.expiresAt || '',
        isFavorite: this.link.isFavorite
      };
    }
  }

  saveChanges() {
    if (!this.link || !this.editData.title || !this.editData.url) {
      this.toastService.show('Title and URL are required.', AppConstants.TOAST_TYPE.WARNING);
      return;
    }

    this.isLoading = true;

    this.linkService.updateLink(this.link.id, this.editData).subscribe({
      next: (updatedLink: ILink) => {
        this.isLoading = false;
        this.toastService.show('Link updated successfully!', AppConstants.TOAST_TYPE.SUCCESS);
        this.linkUpdated.emit(updatedLink);
        this.closeModal();
      },
      error: () => {
        this.isLoading = false;
        this.toastService.show('Failed to update link.', AppConstants.TOAST_TYPE.ERROR);
      }
    });
  }

  closeModal() {
    this.close.emit();
  }
}
