import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ILink, ILinkRequest} from '../../interfaces/link.interface';
import {LinkService} from '../../services/link-service';
import {ToastService} from '../../services/toast-service';
import {Link2, X, Type, LucideAngularModule} from 'lucide-angular';
import {FormsModule} from '@angular/forms';
import {HasValueUtils} from '../../utils/has-value.utils';
import {AppConstants} from '../../constants/app.constants';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-add-link-modal',
  imports: [
    LucideAngularModule,
    FormsModule,
    NgIf
  ],
  templateUrl: './add-link-modal.html',
  styleUrl: './add-link-modal.scss',
})
export class AddLinkModal implements OnInit{

  @Input() isOpen: boolean = false;
  @Output() close = new EventEmitter<void>();
  @Output() linkAdded = new EventEmitter<ILink>();

  readonly CloseIcon = X;
  readonly LinkIcon = Link2;
  readonly TypeIcon = Type;

  isLoading: boolean = false;

  newLink: ILinkRequest = {
    title: '',
    url: '',
    isActive: true
  }

  constructor(
    private linkService: LinkService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {

  }

  saveLink() {
    if(!HasValueUtils.hasValue(this.newLink.title) || !HasValueUtils.hasValue(this.newLink.url)) {
      this.toastService.show(AppConstants.TOAST_MESSAGES.FILL_TITLE_URL, AppConstants.TOAST_TYPE.WARNING);
      return;
    }

    this.isLoading = true;
    this.linkService.createLink(this.newLink).subscribe({
      next: (savedLink: ILink) => {
        this.isLoading = false;
        this.toastService.show(AppConstants.TOAST_MESSAGES.LINK_ADDED_SUCCESSFULLY, AppConstants.TOAST_TYPE.SUCCESS);
        this.linkAdded.emit(savedLink);
        this.resetAndClose();
      }, error: () => {
        this.isLoading = false;
        this.toastService.show(AppConstants.TOAST_MESSAGES.FAILED_TO_CREATE_LINK, AppConstants.TOAST_TYPE.ERROR);
      }
    });
  }

  resetAndClose() {
    // Clear the form for the next time it opens
    this.newLink = { title: '', url: '', isActive: true };
    this.close.emit();
  }
}
