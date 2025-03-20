import { Component } from '@angular/core';
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
  userChoice: string = "";
  choices: string[] = ['Choice A', 'Choice B', 'Choice C', 'Choice D', 'Choice E'];
  correctChoice: number = 0; //index
  submitted: boolean = false;

  validateAnswer(): boolean {
    this.submitted = true;
    return this.userChoice == this.choices[this.correctChoice] ? true : false;
  }

  isCorrect(choice: String): boolean {
    console.log('running isCorrect on ' + choice);
    return this.submitted == true && choice == this.choices[this.correctChoice];
  }

  isIncorrect(choice: String): boolean {
    return this.submitted == true && choice != this.choices[this.correctChoice] && this.userChoice == choice;
  }
}
