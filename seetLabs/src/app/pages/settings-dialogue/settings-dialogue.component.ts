import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import {Clipboard} from '@angular/cdk/clipboard';
import { MatButtonModule } from '@angular/material/button';
import {
  MatDialogContent,
  MatDialogActions,
  MatDialogModule,
  MatDialogClose,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatIcon, MatIconModule } from '@angular/material/icon';
import { TextAreaComponent } from '../../components/text-area/text-area.component';
import { LocalStorageService } from '../../services/local-storage.service';

@Component({
  selector: 'app-settings-dialogue',
  imports: [MatDialogContent,MatDialogActions, MatDialogClose, MatDialogTitle, MatButtonModule, TextAreaComponent],
  templateUrl: './settings-dialogue.component.html',
  styleUrl: './settings-dialogue.component.scss'
})
export class SettingsDialogueComponent {
  
  public message = signal<String>("");

  constructor(
    private clipboard: Clipboard,
    private localStorage: LocalStorageService
  ) {}

  copyKey(): void {
    const key = this.localStorage.getKey();
    this.clipboard.copy(key.toString());
  }

  removeKey(): void {
    this.localStorage.resetKey();
  }

  writeKey(key: String) : void{
    try {
      JSON.parse(atob(key.toString()));
      this.localStorage.updateKey(key.toString());
      this.message.set("Sucessfully updated key.");
    } catch(error: unknown) {
      this.message.set("Failed to update key.");
    }
  }
  
}
