import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Dialog, DialogModule } from '@angular/cdk/dialog'; // <-- Import CDK Dialog
import { CdkDragDrop, CdkDragPreview, CdkDropList, moveItemInArray, CdkDrag, CdkDragHandle } from '@angular/cdk/drag-drop';
import { LinkService } from '../../services/link-service';
import { ToastService } from '../../services/toast-service';
import { Loader } from '../loader/loader';
import { AppConstants } from '../../constants/app.constants';
import { ILink, ILinkRequest } from '../../interfaces/link.interface';
import { AddLinkModal } from '../add-link-modal/add-link-modal';
import {
  Copy, ExternalLink, GripVertical, Plus, Star, Trash2, Link2OffIcon,
  Tag, Calendar, Palette, Link as LinkIcon, Edit2, LucideAngularModule
} from 'lucide-angular';
import {EditLinkModal} from '../edit-link-modal/edit-link-modal';

@Component({
  selector: 'app-link-manager',
  standalone: true,
  imports: [
    CommonModule, FormsModule, NgClass, Loader, LucideAngularModule,
    CdkDropList, CdkDrag, CdkDragHandle, CdkDragPreview, DialogModule
    // Notice: AddLinkModal and EditLinkModal are NO LONGER needed in imports or HTML!
  ],
  templateUrl: './link-manager.html',
  styleUrl: './link-manager.scss',
})
export class LinkManager implements OnInit {
  private dialog = inject(Dialog); // <-- Inject the Dialog Service
  private linkService = inject(LinkService);
  private toastService = inject(ToastService);

  isLoading: boolean = false;
  myLinks: ILink[] = [];

  // --- Curated Vibrant Gradients for Card Accents ---
  private readonly cardGradients = [
    'from-pink-500 via-rose-500 to-amber-400',
    'from-blue-600 via-indigo-500 to-purple-500',
    'from-emerald-400 via-teal-500 to-cyan-600',
    'from-violet-600 via-purple-600 to-pink-500',
    'from-amber-500 via-orange-500 to-red-500',
    'from-cyan-500 via-blue-500 to-indigo-600'
  ];

  // ... (Your existing icon mappings: GripIcon, PlusIcon, etc.) ...
  readonly GripIcon = GripVertical;
  readonly TrashIcon = Trash2;
  readonly PlusIcon = Plus;
  readonly LinkOffIcon = Link2OffIcon;
  readonly LinkIcon = LinkIcon;
  readonly ExternalIcon = ExternalLink;
  readonly StarIcon = Star;
  readonly CopyIcon = Copy;
  readonly TagIcon = Tag;
  readonly CalendarIcon = Calendar;
  readonly PaletteIcon = Palette;
  readonly EditIcon = Edit2;

  ngOnInit(): void {
    this.loadLinks();
  }

  loadLinks() {
    this.isLoading = true;
    this.linkService.getAllLinks().subscribe({
      next: (links) => {
        this.myLinks = links;
        this.isLoading = false;
      },
      error: () => {
        this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_LOAD_LINKS, AppConstants.TOAST_TYPE.ERROR);
        this.isLoading = false;
      }
    });
  }

  // --- PROGRAMMATIC MODAL OPENING ---
  openAddModal() {
    const dialogRef = this.dialog.open<ILink>(AddLinkModal, {
      width: '100%',
      maxWidth: '28rem', // Matches max-w-md
      backdropClass: 'bg-slate-900/20',
      panelClass: 'bg-transparent' // Lets our border-radius & glassmorphism shine through
    });

    // Listen for when the modal closes!
    dialogRef.closed.subscribe((newLink) => {
      if (newLink) {
        this.myLinks.unshift(newLink);
      }
    });
  }

  // --- PROGRAMMATIC MODAL OPENING FOR EDIT ---
  openEditModal(link: ILink) {
    const dialogRef = this.dialog.open<ILink>(EditLinkModal, {
      width: '100%',
      maxWidth: '48rem', // Matches max-w-3xl for our 2-column layout
      backdropClass: 'bg-slate-900/20',
      panelClass: 'bg-transparent',
      data: link // <-- Passes the link object into DIALOG_DATA!
    });

    // Listen for when the user clicks "Save Configuration"
    dialogRef.closed.subscribe((updatedLink) => {
      if (updatedLink) {
        // Find the edited link in our array and replace it with the fresh server data
        const index = this.myLinks.findIndex(l => l.id === updatedLink.id);
        if (index !== -1) {
          this.myLinks[index] = updatedLink;
        }
      }
    });
  }

  // ... (Your remaining helper methods: getCardGradient, quickSaveStatus, deleteLink, etc.) ...
  getCardGradient(index: number): string {
    return this.cardGradients[index % this.cardGradients.length];
  }

  drop(event: CdkDragDrop<ILink[]>) {
    moveItemInArray(this.myLinks, event.previousIndex, event.currentIndex);
  }

  toggleFavorite(link: ILink) {
    // link.isFavorite = !link.isFavorite;
    // this.quickSaveStatus(link);
    const isFavorite = !link.isFavorite;
    link.isFavorite = isFavorite;

    this.linkService.patchFavorite(link.id, isFavorite).subscribe({
      next: (updatedLink) => {
        console.log(AppConstants.TOAST_MESSAGES.LINK_FAVORITE_PATCHED.replace(AppConstants.REPLACEMENTS.ID, String(link.id)).replace(AppConstants.REPLACEMENTS.IS_FAVORITE, String(isFavorite)));
        link.isFavorite = updatedLink.isFavorite;
      },
      error: () => {
        link.isFavorite = !isFavorite;
        this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_UPDATE_FAVORITE_STATUS, AppConstants.TOAST_TYPE.ERROR);
      }
    });
  }

  quickSaveStatus(link: ILink) {
    const updateReq: ILinkRequest = {
      title: link.title,
      url: link.url,
      isActive: link.isActive,
      label: link.label,
      colorCode: link.colorCode,
      customSlug: link.customSlug,
      expiresAt: link.expiresAt,
      isFavorite: link.isFavorite
    };

    this.linkService.patchStatus(link.id, updateReq.isActive).subscribe({
      next: () => console.log(AppConstants.TOAST_MESSAGES.QUICK_SAVED.replace(AppConstants.REPLACEMENTS.ID, String(link.id))),
      error: () => this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_SAVE_STATUS, AppConstants.TOAST_TYPE.ERROR)
    });
  }

  deleteLink(id: number) {
    if (!confirm(AppConstants.TOAST_MESSAGES.ARE_SURE_YOU_WANT_T0_DELETE_LINK)) return;

    this.linkService.deleteLink(id).subscribe({
      next: () => {
        this.myLinks = this.myLinks.filter(link => link.id !== id);
        this.toastService.show(AppConstants.TOAST_MESSAGES.LINK_DELETED, AppConstants.TOAST_TYPE.SUCCESS);
      },
      error: () => this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_DELETE_LINK, AppConstants.TOAST_TYPE.ERROR)
    });
  }

  copyShortLink(shortCode: string, customSlug?: string) {
    const activeCode = customSlug ? customSlug : shortCode;
    if (!activeCode) return;
    navigator.clipboard.writeText(AppConstants.URLs.HTTPS_TAP_LINKS.replace(AppConstants.REPLACEMENTS.ACTIVE_CODE, String(activeCode)));
    this.toastService.show(AppConstants.TOAST_MESSAGES.COPIED_TO_CLIPBOARD, AppConstants.TOAST_TYPE.INFO);
  }
}
