import { Component, Input} from '@angular/core';
import { ProgressBarComponent } from '../progress-bar/progress-bar.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'module-sub-header',
  imports: [CommonModule, ProgressBarComponent, MatIconModule, MatButtonModule],
  templateUrl: './module-sub-header.component.html',
  styleUrl: './module-sub-header.component.scss'
})
export class ModuleSubHeaderComponent {
  @Input() headerBottom = false;
  @Input() goForward = true;
  @Input() goBack = true;
  @Input() moduleNumber = '';
  @Input() assignmentNumber = '';

  incrementAssignment() : string {
    const num = parseInt(this.assignmentNumber);
    return (num+1).toString();
  }

  decrementAssignment() : string {
    const num = parseInt(this.assignmentNumber);
    return (num-1).toString();
  }
}
