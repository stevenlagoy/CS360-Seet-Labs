import { Component, Input } from '@angular/core';
import { MatProgressBar } from '@angular/material/progress-bar';

@Component({
  selector: 'progress-bar',
  imports: [MatProgressBar],
  templateUrl: './progress-bar.component.html',
  styleUrl: './progress-bar.component.scss'
})
export class ProgressBarComponent {
  @Input() value = ''
  @Input() title = ''
}
