import { Component, Input, ViewEncapsulation, signal } from '@angular/core';
import { MatFormField } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'text-area',
  imports: [MatFormField, MatInputModule, MatIcon, CommonModule, FormsModule],
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
  @Input() label = '';
  @Input() initialValue='';

  public content = signal<String>("");

  // public content:string = "";

  ngOnInit(){
    this.content.set(this.initialValue);
  }
}

