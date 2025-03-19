import { Component } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { GradientHeaderComponent } from '../../components/gradient-header/gradient-header.component';
import { QuizCardComponent } from '../../components/quiz-card/quiz-card.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { ProgressBarComponent } from '../../components/progress-bar/progress-bar.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TextAreaComponent } from '../../components/text-area/text-area.component';
import { InputFieldComponent } from '../../components/input-field/input-field.component';


@Component({
  selector: 'app-components',
  imports: [MatButtonModule,
    MatIconModule,
    GradientHeaderComponent,
    QuizCardComponent,
    MatProgressBar,
    MatProgressSpinnerModule,
    ProgressBarComponent,
    MatFormFieldModule,
    MatInputModule,
    InputFieldComponent,
    TextAreaComponent
  ],
  templateUrl: './components.component.html',
  styleUrl: './components.component.scss'
})
export class ComponentsComponent {

}
