import { CommonModule } from '@angular/common';
import { Input } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {Component} from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatDividerModule} from '@angular/material/divider';
import {MatProgressBarModule} from '@angular/material/progress-bar';

@Component({
  selector: 'input-button',
  // standalone: true,
  templateUrl: './input-button.component.html',
  styleUrl: './input-button.component.css',
  imports: [MatButtonModule, MatIconModule, MatDividerModule, MatProgressBarModule]
})

export class InputButtonComponent {
  // @Input() label = '';
  // @Input() textColor = '';
  // @Input() buttonColor = '';
}
