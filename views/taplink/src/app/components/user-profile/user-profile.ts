import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Clock, Image, LucideAngularModule, MapPin, User, X} from 'lucide-angular';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AppConstants} from '../../constants/app.constants';

@Component({
  selector: 'app-user-profile',
  imports: [
    LucideAngularModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss',
})
export class UserProfile implements OnInit{

  @Input() isOpen: boolean = false;

  @Output() close = new EventEmitter<void>();

  isLoading = false

  worldTimeZones = Intl.supportedValuesOf(AppConstants.TIMEZONE);

  locationResults : any[] = [];

  searchTimeout: any;

  readonly CloseIcon = X;
  readonly UserIcon = User;
  readonly MapPinIcon = MapPin;
  readonly ClockIcon = Clock;
  readonly ImageIcon = Image;

  userProfile = {
    firstName: 'John',
    lastName: 'Doe',
    bio: 'Software Engineer & Creator',
    location: 'San Francisco, CA',
    timezone: 'PST',
    profilePictureUrl: ''
  };

  ngOnInit(): void {

  }

  onLocationSearch(event: any) {
    const query = event.target.value;
    this.userProfile.location = query;

    clearTimeout(this.searchTimeout);

    if(query.length > 6) {
      this.locationResults = [];
      return;
    }

    this.searchTimeout = setTimeout(async () => {
      try {
        const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&limit=5`);
        this.locationResults = await response.json();
      } catch (error) {
        console.error('location search failed', error);
      }
    }, 400);
  }

  selectLocation(loc: any) {
    // When they click a dropdown result, set it and clear the menu
    this.userProfile.location = loc.display_name;
    this.locationResults = [];
  }

  onSave() {
    this.isLoading = false;
  }
}
