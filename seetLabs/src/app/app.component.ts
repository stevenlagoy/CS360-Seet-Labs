import { Component } from '@angular/core';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { GradientHeaderComponent } from './components/gradient-header/gradient-header.component';

@Component({
  selector: 'app-root',
  imports: [ProgressBarComponent, MatButtonModule, MatIconModule, GradientHeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'seetLabs';
}
