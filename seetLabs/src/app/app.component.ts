import { Component, OnInit} from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { RouterOutlet } from '@angular/router';
import { LocalStorageService } from './services/local-storage.service';

@Component({
  selector: 'app-root',
  imports: [HeaderComponent, FooterComponent, RouterOutlet],
  styleUrl: './app.component.scss',
  template: `
    <app-header />
    <main class="main-container">
      <router-outlet />
    </main>
    <app-footer>
  `,
  // encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {
  title : String  = 'seetLabs';
  
  ngOnInit(): void {
    new LocalStorageService();
  }
}
