import { Component } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { GradientHeaderComponent } from './components/gradient-header/gradient-header.component';
import { QuizCardComponent } from './components/quiz-card/quiz-card.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';

@Component({
  selector: 'app-root',
  imports: [QuizCardComponent, ProgressBarComponent, MatProgressSpinnerModule, MatButtonModule, MatIconModule, GradientHeaderComponent, MatProgressBar],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'seetLabs';
}
