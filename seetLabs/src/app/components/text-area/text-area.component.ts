import { Component, Input, ViewEncapsulation } from '@angular/core';
import { MatFormField } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'text-area',
  imports: [MatFormField, MatInputModule, MatIcon, CommonModule],
  templateUrl: './text-area.component.html',
  styleUrl: './text-area.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class TextAreaComponent {
  @Input() key = '';
  @Input() width = '';
  @Input() height = '';
  @Input() placeholder = '';
  @Input() readonly = false;
  @Input() value = '';
  @Input() label = '';
}
