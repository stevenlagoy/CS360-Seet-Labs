import { Component, Input, OnInit, inject, signal} from '@angular/core';
import { ProgressBarComponent } from '../progress-bar/progress-bar.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { firstValueFrom } from 'rxjs';

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
  @Input() modulePercetage = 0;
  @Input() activityPassed = false;
  goForward = signal<Boolean>(true);
  goBack = signal<Boolean>(true);
  
  numberAssignments = signal<number>(0);
  numberModules = signal<number>(10);
  lastModuleLastActivityNum = signal<number>(0);
  getDataService = inject(JsonServerTestService);

  async ngOnInit(): Promise<void> {
    this.numberAssignments.set(await this.getDataService.getModuleContents(this.moduleNumber)-1);
    this.lastModuleLastActivityNum.set(await this.getDataService.getModuleContents(this.decrementModule())-1);
  }

  incrementAssignment() : string {
    const num = parseInt(this.assignmentNumber);
    return (num+1).toString();
  }

  decrementAssignment() : string {
    const num = parseInt(this.assignmentNumber);
    return (num-1).toString();
  }

  incrementModule() : string {
    const num = parseInt(this.moduleNumber);
    return (num+1).toString();
  }

  decrementModule() : string {
    const num = parseInt(this.moduleNumber);
    return (num-1).toString();
  }

  getAssignmentNumber(): number {
    return Number(this.assignmentNumber);
  }
}
