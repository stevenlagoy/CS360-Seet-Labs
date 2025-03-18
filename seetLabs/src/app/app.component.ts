import { Component, ViewEncapsulation } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { GradientHeaderComponent } from './components/gradient-header/gradient-header.component';
import { QuizCardComponent } from './components/quiz-card/quiz-card.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TextAreaComponent } from './components/text-area/text-area.component';
import { InputFieldComponent } from './components/input-field/input-field.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';

@Component({
  selector: 'app-root',
  imports: [MatButtonModule,
    MatIcon,
    MatIconModule,
    GradientHeaderComponent,
    QuizCardComponent,
    MatProgressBar,
    MatProgressSpinnerModule,
    ProgressBarComponent,
    MatFormFieldModule,
    MatInputModule,
    InputFieldComponent,
    TextAreaComponent,
    HeaderComponent,
    FooterComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  // encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  title = 'seetLabs';
}
