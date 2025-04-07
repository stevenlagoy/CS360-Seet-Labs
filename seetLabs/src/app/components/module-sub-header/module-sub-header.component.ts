import { Component, Input, OnInit, signal} from '@angular/core';
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

export class ModuleSubHeaderComponent implements OnInit {
  @Input() headerBottom = false;
  @Input() moduleNumber = '';
  @Input() assignmentNumber = '';
  goForward = signal<Boolean>(true);
  goBack = signal<Boolean>(true);

  ngOnInit(): void {
    if (this.assignmentNumber == "1"){
      this.goBack.set(false);
    } else if (this.assignmentNumber == "5"){
      this.goForward.set(false);
    }
  }

  incrementAssignment() : string {
    const num = parseInt(this.assignmentNumber);
    return (num+1).toString();
  }

  decrementAssignment() : string {
    const num = parseInt(this.assignmentNumber);
    return (num-1).toString();
  }
}
