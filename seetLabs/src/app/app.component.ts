import { Component } from '@angular/core';
// import { InputButtonComponent } from './components/input-button/input-button.component';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  imports: [ProgressBarComponent, MatButtonModule, MatIconModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'seetLabs';
}
