import { Component, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';
import { TextAreaComponent } from '../text-area/text-area.component';
import { LocalStorageService } from '../../services/local-storage.service';

@Component({
  selector: 'app-dialog-box',
  imports: [MatDialogContent, TextAreaComponent, MatDialogActions, MatDialogClose, MatDialogTitle, MatButtonModule, TextAreaComponent],
  templateUrl: './dialog-box.component.html',
  styleUrl: './dialog-box.component.scss'
})
export class DialogBoxComponent {
  public message = signal<String>("");

  constructor(private localStorage: LocalStorageService) {}

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
