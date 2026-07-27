import { Component, OnInit } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, CdkDragPreview, CdkDropList, moveItemInArray, CdkDrag, CdkDragHandle } from '@angular/cdk/drag-drop';
import { LinkService } from '../../services/link-service';
import { ToastService } from '../../services/toast-service';
import { Loader } from '../loader/loader';
import { AppConstants } from '../../constants/app.constants';
import { ILink, ILinkRequest } from '../../interfaces/link.interface';
import {
  Copy, ExternalLink, GripVertical, Plus, Star, Trash2, Link2OffIcon, LucideAngularModule
} from 'lucide-angular';
import {AddLinkModal} from '../add-link-modal/add-link-modal';

@Component({
  selector: 'app-link-manager',
  standalone: true,
  imports: [
    CommonModule, FormsModule, NgClass, Loader, LucideAngularModule,
    CdkDropList, CdkDrag, CdkDragHandle, CdkDragPreview, AddLinkModal
  ],
  templateUrl: './link-manager.html',
  styleUrl: './link-manager.scss',
})
export class LinkManager implements OnInit {
  isLoading: boolean = false;
  isAddModalOpen = false;

  // --- Icons ---
  readonly GripIcon = GripVertical;
  readonly TrashIcon = Trash2;
  readonly PlusIcon = Plus;
  readonly LinkIcon = Link2OffIcon;
  readonly ExternalIcon = ExternalLink;
  readonly StarIcon = Star;
  readonly CopyIcon = Copy;

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
        this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_LOAD_LINKS, AppConstants.TOAST_TYPE.ERROR);
        this.isLoading = false;
      }
    });
  }

  openAddModal() {
    this.isAddModalOpen = true;
  }

  // Receives the new link from the modal and puts it in the table
  onLinkAdded(newLink: ILink) {
    this.myLinks.unshift(newLink);
  }

  onLinkEdited(link: ILink) {
    const updateReq: ILinkRequest = {
      title: link.title,
      url: link.url,
      isActive: link.isActive
    };

    this.linkService.updateLink(link.id, updateReq).subscribe({
      next: () => console.log(`Link ${link.id} saved.`),
      error: () => this.toastService.show('Failed to save changes', AppConstants.TOAST_TYPE.ERROR)
    });
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
    // Note: Backend persistence for reordering will go here later
  }

  toggleFavorite(link: ILink) {
    link.isFavorite = !link.isFavorite;
    // Note: Update logic for presentation data will go here later
  }

  copyShortLink(shortCode: string) {
    if (!shortCode) return;
    navigator.clipboard.writeText(`https://tap.link/${shortCode}`);
    this.toastService.show('Copied to clipboard!', AppConstants.TOAST_TYPE.INFO);
  }
}
