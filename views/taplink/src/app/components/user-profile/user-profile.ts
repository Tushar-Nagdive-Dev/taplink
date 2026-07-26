import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {AlertTriangle, Clock, Image, LucideAngularModule, MapPin, User, X} from 'lucide-angular';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AppConstants} from '../../constants/app.constants';
import {UserService} from '../../services/user-service';
import {IUserProfile} from '../../interfaces/user.interface';
import {ToastService} from '../../services/toast-service';

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
export class UserProfile implements OnInit, OnChanges{
  /*Boolean Elements S*/
  @Input() isOpen: boolean = false;
  isLoading = false;
  isFetchingData: boolean = false;
  showPiiWarning: boolean = false;
  isTimezoneDropdownOpen: boolean = false;
  /*Boolean Elements E*/

  @Output() close = new EventEmitter<void>();
  worldTimeZones = Intl.supportedValuesOf(AppConstants.TIMEZONE);
  locationResults : any[] = [];
  filteredTimezones: string[] = [];
  originalProfile: IUserProfile | null = null;
  searchTimeout: any;

  protected readonly AppConstants = AppConstants;
  readonly CloseIcon = X;
  readonly UserIcon = User;
  readonly MapPinIcon = MapPin;
  readonly ClockIcon = Clock;
  readonly ImageIcon = Image;
  readonly AlertIcon = AlertTriangle;

  userProfile: IUserProfile = {
    firstName: 'John',
    lastName: 'Doe',
    bio: 'Software Engineer & Creator',
    location: 'San Francisco, CA',
    timezone: 'PST',
    profilePictureUrl: ''
  };

  constructor(
    private userService: UserService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.getUserProfile();
  }

  ngOnChanges(changes:SimpleChanges): void {
    if (changes['isOpen'] && changes['isOpen'].currentValue === true) {
      this.getUserProfile();
    } else {
      this.showPiiWarning = false;
      this.isTimezoneDropdownOpen = false;
      this.locationResults = [];
    }
  }

  getUserProfile() {
    this.userService.getUserProfile().subscribe({
      next: response => {
        this.userProfile = { ...response };
        this.originalProfile = JSON.parse(JSON.stringify(response));
        this.isFetchingData = false;
      },
      error: error => {
        console.log(error);
        this.isFetchingData = false;
      }
    });
  }

  onLocationSearch(event: any) {
    const query = event.target.value;
    this.userProfile.location = query;

    clearTimeout(this.searchTimeout);

    if(query.length < 3) {
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

  onTimezoneSearch(event: any) {
    const query = event.target.value.toLowerCase();
    this.userProfile.timezone = event.target.value;
    this.isTimezoneDropdownOpen = true;
    if(!query) {
      this.filteredTimezones = this.worldTimeZones;
      return;
    }
    this.filteredTimezones = this.worldTimeZones.filter(tz => tz.toLowerCase().includes(query));
  }

  selectTimezone(tz: string) {
    this.userProfile.timezone = tz;
    this.isTimezoneDropdownOpen = false;
  }

  attemptSave() {
    if(!this.originalProfile) return;

    const isNameChanged = this.userProfile.firstName !== this.originalProfile.firstName || this.userProfile.lastName !== this.originalProfile.lastName;
    if(isNameChanged && !this.showPiiWarning) {
      this.showPiiWarning = true;
    } else {
      this.onSave();
    }
  }

  cancelWarning() {
    this.showPiiWarning = false;
    if(this.originalProfile) {
      this.userProfile.timezone = this.originalProfile.firstName;
      this.userProfile.lastName = this.originalProfile.lastName;
    }
  }

  onSave() {
    this.isLoading = false;
    this.userService.updateUserProfile(this.userProfile).subscribe({
      next: response => {
        this.userProfile = { ...response };
        this.toastService.show(AppConstants.TOAST_USER_PROFILE_SAVE_MSG, AppConstants.TOAST_TYPE.SUCCESS);
        this.close.emit();
      },
      error: err => this.toastService.show(AppConstants.TOAST_USER_PROFILE_SAVE_FAILED, AppConstants.TOAST_TYPE.ERROR)
    });
  }
}
