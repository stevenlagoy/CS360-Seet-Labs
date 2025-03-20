import { Component, Input, ViewEncapsulation } from '@angular/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';


@Component({
  selector: 'app-input-field',
  imports: [MatFormFieldModule, MatInputModule, MatIcon],
  templateUrl: './input-field.component.html',
  styleUrl: './input-field.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class InputFieldComponent {
  @Input() placeholder = '';
  @Input() label = '';
  @Input() key = '';
  @Input() readonly = false;
  @Input() value = '';
  @Input() labelVisible = true;
}


