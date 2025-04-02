import { Component, Input } from '@angular/core';
import {MatRadioModule} from '@angular/material/radio';
import {FormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'quiz-card',
  imports: [CommonModule, FormsModule, MatRadioModule, MatCardModule, MatButtonModule],
  templateUrl: './quiz-card.component.html',
  styleUrl: './quiz-card.component.scss'
})
export class QuizCardComponent {

  userChoice: number = -1;
  correctOptionsSet: Set<number> = new Set<number>;
  submitted: boolean = false;
  correct: boolean = false;
  @Input() title: string = "";
  @Input() questionTitle: string = "";
  @Input() choices: string[] = [];
  @Input() points: string = "0";
  @Input() correctChoices: string[] = [];
  @Input() feedback: string[] = [];

  ngOnInit() : void {
    for (let i = 0; i <= this.correctChoices.length; i++){
      this.correctOptionsSet.add(parseInt(this.correctChoices[i]));
    }
  }

  validateAnswer(): boolean {
    this.submitted = true;
    this.correct = this.correctOptionsSet.has(this.userChoice+1) ? true : false;
    return this.correctOptionsSet.has(this.userChoice+1) ? true : false;
  }

  parseInt(arg: string): number {
    console.log(arg);
    return this.parseInt(arg);
  }

  parseStr(arg: number) : string {
    return arg.toString();
  }
}
