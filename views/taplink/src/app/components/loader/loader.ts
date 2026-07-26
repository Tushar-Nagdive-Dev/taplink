import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loader.html',
  styleUrl: './loader.scss', // You can leave the SCSS file completely empty!
})
export class Loader {
  @Input() message: string = 'Loading...'; // Added a default fallback message
}
