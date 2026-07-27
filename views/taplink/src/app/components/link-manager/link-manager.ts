import { Component, OnInit } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, CdkDragPreview, CdkDropList, moveItemInArray, CdkDrag, CdkDragHandle } from '@angular/cdk/drag-drop';
import { LinkService } from '../../services/link-service';
import { ToastService } from '../../services/toast-service';
import { Loader } from '../loader/loader';
import { AppConstants } from '../../constants/app.constants';
import { ILink, ILinkRequest } from '../../interfaces/link.interface';
import { AddLinkModal } from '../add-link-modal/add-link-modal';
import { EditLinkModal } from '../edit-link-modal/edit-link-modal';
import {
  Copy, ExternalLink, GripVertical, Plus, Star, Trash2, Link2OffIcon,
  Tag, Calendar, Palette, Link as LinkIcon, Edit2, LucideAngularModule
} from 'lucide-angular';

@Component({
  selector: 'app-link-manager',
  standalone: true,
  imports: [
    CommonModule, FormsModule, NgClass, Loader, LucideAngularModule,
    CdkDropList, CdkDrag, CdkDragHandle, CdkDragPreview, AddLinkModal, EditLinkModal
  ],
  templateUrl: './link-manager.html',
  styleUrl: './link-manager.scss',
})
export class LinkManager implements OnInit {
  isLoading: boolean = false;
  isAddModalOpen: boolean = false;
  isEditModalOpen: boolean = false;

  // We will pass this to the modal when editing an existing link
  selectedLinkToEdit: ILink | null = null;

  // --- Icons ---
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

  myLinks: ILink[] = [];

  constructor(
    private linkService: LinkService,
    private toastService: ToastService
  ) {}

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
        this.toastService.show('Failed to load links', AppConstants.TOAST_TYPE.ERROR);
        this.isLoading = false;
      }
    });
  }

  openAddModal() {
    this.isAddModalOpen = true;
  }

  openEditModal(link: ILink) {
    this.selectedLinkToEdit = link;
    this.isEditModalOpen = true; // Fixed: Opens the Edit Modal instead of Add Modal
  }

  onLinkAdded(newLink: ILink) {
    // Instantly add the new link to the top of the table
    this.myLinks.unshift(newLink);
  }

  onLinkUpdated(updatedLink: ILink) {
    // Find the edited link in our array and replace it with the fresh data from the server
    const index = this.myLinks.findIndex(l => l.id === updatedLink.id);
    if (index !== -1) {
      this.myLinks[index] = updatedLink;
    }
  }

  // Used only for the quick-toggle switches (Active & Favorite)
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

    this.linkService.updateLink(link.id, updateReq).subscribe({
      next: () => console.log(`Quick-saved link ${link.id}`),
      error: () => this.toastService.show('Failed to save status', AppConstants.TOAST_TYPE.ERROR)
    });
  }

  toggleFavorite(link: ILink) {
    link.isFavorite = !link.isFavorite;
    this.quickSaveStatus(link);
  }

  deleteLink(id: number) {
    if(!confirm('Are you sure you want to delete this link?')) return;

    this.linkService.deleteLink(id).subscribe({
      next: () => {
        this.myLinks = this.myLinks.filter(link => link.id !== id);
        this.toastService.show('Link deleted', AppConstants.TOAST_TYPE.SUCCESS);
      },
      error: () => this.toastService.show('Failed to delete link', AppConstants.TOAST_TYPE.ERROR)
    });
  }

  drop(event: CdkDragDrop<ILink[]>) {
    moveItemInArray(this.myLinks, event.previousIndex, event.currentIndex);
  }

  copyShortLink(shortCode: string, customSlug?: string) {
    const activeCode = customSlug ? customSlug : shortCode;
    if (!activeCode) return;
    navigator.clipboard.writeText(`https://tap.link/${activeCode}`);
    this.toastService.show('Copied to clipboard!', AppConstants.TOAST_TYPE.INFO);
  }
}
