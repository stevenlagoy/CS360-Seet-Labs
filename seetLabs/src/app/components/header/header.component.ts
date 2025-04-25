import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { GradientHeaderComponent } from '../gradient-header/gradient-header.component';
import { SettingsDialogueComponent } from '../../pages/settings-dialogue/settings-dialogue.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-header',
  imports: [MatButtonModule, GradientHeaderComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  private dialogBox = inject(MatDialog);

  openSettingsPage(): void {

    const setting_session_open = this.dialogBox.getDialogById('settings-dialog');
    if (setting_session_open){
      return //return early if user already has settings dialog open
    }

    this.dialogBox.open(SettingsDialogueComponent, {
      id: 'settings-dialog'
    });
  }

}
