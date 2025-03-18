import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { GradientHeaderComponent } from '../gradient-header/gradient-header.component';

@Component({
  selector: 'app-header',
  imports: [MatButtonModule, GradientHeaderComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

}
