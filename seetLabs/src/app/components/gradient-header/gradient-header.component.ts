import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'gradient-header',
  imports: [CommonModule],
  templateUrl: './gradient-header.component.html',
  styleUrl: './gradient-header.component.css'
})
export class GradientHeaderComponent {
  @Input() label = '';
  @Input() size = '';
}
