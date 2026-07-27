import {Component, OnInit} from '@angular/core';
import {LinkService} from '../../services/link-service';
import {ToastService} from '../../services/toast-service';
import {Loader} from '../loader/loader';
import {
  Copy,
  ExternalLink,
  GripVertical,
  MoreHorizontal,
  Plus,
  Share2,
  Star,
  Trash2,
  Link2OffIcon,
  LucideAngularModule
} from 'lucide-angular';
import {ILink, ILinkRequest} from '../../interfaces/link.interface';
import {AppConstants} from '../../constants/app.constants';
import {FormsModule} from '@angular/forms';
import {CommonModule, NgClass} from '@angular/common';
import {CdkDragDrop, CdkDragPreview, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-link-manager',
  imports: [
    Loader,
    LucideAngularModule,
    FormsModule,
    NgClass,
    CdkDropList,
    CommonModule,
    CdkDragPreview
  ],
  templateUrl: './link-manager.html',
  styleUrl: './link-manager.scss',
})
export class LinkManager implements OnInit{

  isLoading: boolean = false;
  // --- Icons ---
  readonly GripIcon = GripVertical;
  readonly TrashIcon = Trash2;
  readonly PlusIcon = Plus;
  readonly LinkIcon = Link2OffIcon;
  readonly ExternalIcon = ExternalLink;
  readonly StarIcon = Star;
  readonly CopyIcon = Copy;
  readonly ShareIcon = Share2;
  readonly MenuIcon = MoreHorizontal;
  myLinks: ILink[] = [];

  constructor(
    private linkService: LinkService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {

  }

  // --- READ ---
  loadLinks() {
    this.isLoading = true;
    this.linkService.getAllLinks().subscribe({
      next: (links) => {
        this.myLinks = links;
        this.isLoading = false;
      },
      error: (err) => {
        this.toastService.show('Failed to load links', AppConstants.TOAST_TYPE.ERROR);
        this.isLoading = false;
      }
    });
  }

  // --- CREATE ---
  addNewLink() {
    const newLinkReq: ILinkRequest = {
      title: 'New Link',
      url: '',
      isActive: true
    };

    this.linkService.createLink(newLinkReq).subscribe({
      next: (savedLink) => {
        // Add the newly created link (with real DB ID and shortCode) to the top
        this.myLinks.unshift(savedLink);
        this.toastService.show('Link created successfully', AppConstants.TOAST_TYPE.SUCCESS);
      },
      error: () => this.toastService.show('Could not create link', AppConstants.TOAST_TYPE.ERROR)
    });
  }

  // --- UPDATE (Auto-Save) ---
  onLinkEdited(link: ILink) {
    const updateReq: ILinkRequest = {
      title: link.title,
      url: link.url,
      isActive: link.isActive
    };

    this.linkService.updateLink(link.id, updateReq).subscribe({
      next: () => {
        console.log(`Link ${link.id} auto-saved.`);
      },
      error: () => this.toastService.show('Failed to save changes')
    });
  }

  // --- DELETE ---
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

  // --- UI Helpers ---
  drop(event: CdkDragDrop<ILink[]>) {
    moveItemInArray(this.myLinks, event.previousIndex, event.currentIndex);
    // Note: Phase 2 will add the backend API call here to save the new positions!
  }

  toggleFavorite(link: ILink) {
    link.isFavorite = !link.isFavorite;
    // UI toggle only for now
  }

  copyShortLink(shortCode: string) {
    const fullUrl = `https://tap.link/${shortCode}`;
    navigator.clipboard.writeText(fullUrl);
    this.toastService.show('Copied to clipboard!', AppConstants.TOAST_TYPE.INFO);
  }
}
