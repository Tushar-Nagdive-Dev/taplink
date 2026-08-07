import {Component, OnInit} from '@angular/core';
import {LinkService} from '../../services/link-service';
import {ILink} from '../../interfaces/link.interface';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-qr-code',
  imports: [
    NgIf,
    FormsModule,
    NgForOf
  ],
  standalone: true,
  templateUrl: './qr-code.html',
  styleUrl: './qr-code.scss',
})
export class QrCode implements OnInit{

  // States
  allLinks: any[] = [];
  filteredLinks: any[] = [];
  searchQuery: string = '';
  isDropdownOpen: boolean = false;
  isAddingNew: boolean = false;
  isLoading: boolean = false;

  // Selected Target Link & Configurations
  selectedLink: any = null;
  qrImageBlobUrl: string | null = null;

  // Form Configurations matching QrBarcodeConfig entity fields
  codeType: string = 'QR_CODE';
  errorCorrection: string = 'MEDIUM';
  foregroundColor: string = '#000000';
  backgroundColor: string = '#FFFFFF';
  size: number = 300;
  margin: number = 1;
  includeLogo: boolean = false;
  logoUrl: string = '';

  constructor(
    private linkService: LinkService
  ) {}

  ngOnInit(): void {
    this.fetchUserLinks();
  }

  fetchUserLinks() {
    // Use your dedicated LinkService method
    this.linkService.getAllLinks().subscribe({
      next: (links) => {
        this.allLinks = links;
        this.filteredLinks = links;
      },
      error: (err) => console.error('Failed to load user links', err)
    });
  }

  filterLinks() {
    this.isDropdownOpen = true;
    const query = this.searchQuery.toLowerCase();
    this.filteredLinks = this.allLinks.filter(link =>
      (link.title && link.title.toLowerCase().includes(query)) ||
      (link.originalUrl && link.originalUrl.toLowerCase().includes(query)) ||
      (link.shortCode && link.shortCode.toLowerCase().includes(query))
    );
  }

  selectLink(link: ILink) {
    this.selectedLink = link;
    this.searchQuery = link.title || link.url;
    this.isDropdownOpen = false;
    this.isAddingNew = false;
    this.loadQrCode(link.id);
  }

  loadQrCode(linkId: number) {
    this.isLoading = true;
    this.linkService.getQrCodeImage(linkId).subscribe({
      next: (blob) => {
        if (this.qrImageBlobUrl) {
          URL.revokeObjectURL(this.qrImageBlobUrl);
        }
        this.qrImageBlobUrl = URL.createObjectURL(blob);
        this.isLoading = false;
      },
      error: () => {
        this.qrImageBlobUrl = null;
        this.isLoading = false;
      }
    });
  }

  saveAndUpdateConfig() {
    if (!this.selectedLink) return;

    this.isLoading = true;
    const payload = {
      codeType: this.codeType,
      errorCorrection: this.errorCorrection,
      foregroundColor: this.foregroundColor,
      backgroundColor: this.backgroundColor,
      size: this.size,
      margin: this.margin,
      includeLogo: this.includeLogo,
      logoUrl: this.includeLogo ? this.logoUrl : null,
      isActive: true
    };

    this.linkService.updateQrConfig(this.selectedLink.id, payload).subscribe({
      next: () => {
        this.loadQrCode(this.selectedLink!.id);
      },
      error: (err) => {
        console.error('Failed to save configuration', err);
        this.isLoading = false;
      }
    });
  }

}
