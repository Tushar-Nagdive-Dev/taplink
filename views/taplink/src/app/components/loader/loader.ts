import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LucideAngularModule} from 'lucide-angular';

@Component({
  selector: 'app-loader',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './loader.html',
  styleUrl: './loader.scss',
})
export class Loader {
  @Input() message: string = '';
}
