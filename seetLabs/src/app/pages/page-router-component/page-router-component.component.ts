import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { catchError } from 'rxjs';
import { CommonModule, NgIf } from '@angular/common';
import { CodingActivityComponent } from '../coding-activity/coding-activity.component';


@Component({
  standalone: true,
  selector: 'app-page-router-component',
  imports: [CommonModule, NgIf],
  template: `
    <ng-container *ngIf="componentToRender">
      <ng-container *ngComponentOutlet="componentToRender"></ng-container>
    </ng-container>
  `
})
export class PageRouterComponentComponent {
  private route = inject(ActivatedRoute);
  private getDataServer = inject(JsonServerTestService);
  componentToRender: any;

  async ngOnInit() {
    const moduleNum = this.route.snapshot.paramMap.get("id");
    const assignmentNumber = this.route.snapshot.paramMap.get("assignmentNumber");
    let assignmentType: number = -1;

    this.getDataServer.getDataFromAPI(moduleNum, assignmentNumber).pipe(
      catchError((err) => {
        console.log(err);
        throw err;
      })
    ).subscribe(async (data) => {
      assignmentType = data.type;

       //load correct component:
      if (assignmentType === 0){
        const { readingActivity} = await import('../reading-activity/reading-activity');
        this.componentToRender = readingActivity;
      } else if (assignmentType === 1){
        const { QuizActivityComponent } = await import("../quiz-activity/quiz-activity.component");
        this.componentToRender = QuizActivityComponent;
      } else if (assignmentType === 3){
        const { CodingActivityComponent } = await import("../coding-activity/coding-activity.component");
        this.componentToRender = CodingActivityComponent;
      }
    })    
  } 
}
