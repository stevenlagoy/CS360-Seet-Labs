import { Component, OnInit, ViewChildren, signal } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { catchError } from 'rxjs';
import { questionType } from '../../models/questionType';
import { ModuleSubHeaderComponent } from '../../components/module-sub-header/module-sub-header.component';
import { CommonModule } from '@angular/common';
import { QuizCardComponent } from '../../components/quiz-card/quiz-card.component';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-quiz-activity',
  imports: [ModuleSubHeaderComponent, MatButtonModule, MatIconModule, CommonModule, QuizCardComponent],
  templateUrl: './quiz-activity.component.html',
  styleUrl: './quiz-activity.component.scss'
})
export class QuizActivityComponent implements OnInit {

  constructor(private _route: ActivatedRoute) {}
  
  questions = signal<questionType[]>([]);

  jsonDataService = inject(JsonServerTestService);
  quizName = signal<string>('')
  totalPoints = signal(0);
  earnedPoints = signal(0);
  numQuestions : number = 0;
  percentage = signal(0);
  submitted = signal(false);

  ngOnInit(): void {
    const module = this._route.snapshot.paramMap.get('id');
    const assignmentNumber = this._route.snapshot.paramMap.get('assignmentNumber');
    this.jsonDataService.getDataFromAPI(module, assignmentNumber).pipe(
      catchError((err) => {
        console.error(err);
        throw err;
      }))
      .subscribe(async (data: any) => {
        this.quizName.set(data.questions.name);
        this.populateQuestions(data.questions);
      })
    
  }

  populateQuestions(questions: any) : void {
    this.numQuestions = Object.keys(questions.content).length;
    for (let i = 1; i <= Object.keys(questions.content).length; i++){ //
      let question : questionType = {
        'type': questions.content[i.toString()].type,
        'question': questions.content[i.toString()].question,
        'options': this.arrayStringify(questions.content[i.toString()].options),
        'correct_responses': questions.content[i.toString()].correct_responses,
        'points': questions.content[i.toString()].points,
        'feedback': this.arrayStringify(questions.content[i.toString()].feedback)
      }

      //update questions array;
      this.questions.update(qs => [...qs, question])
    }
  }

  arrayStringify(data: any): string[] {
    let arr : string[] = [];
    for (let i = 1; i <= Object.keys(data).length; i++){
      arr.push(data[i.toString()]);
    }
    return arr;
  }

  @ViewChildren(QuizCardComponent) quizCards!: QuizCardComponent[];

  checkQuizResults(): void {
    this.submitted.set(true);
    let correctCount: number = 0;
    this.quizCards.forEach(card => {
      this.totalPoints.update(val => val + parseInt(card.points));
      if (card.validateAnswer()){
        this.earnedPoints.update(val => val + parseInt(card.points));

      }
    })
    this.percentage.set(Math.round((this.earnedPoints() / this.totalPoints()) * 100));
  }
}
