import { Component, Input, ViewChild } from '@angular/core';
import { CodeControlComponent } from '../code-control/code-control.component';
import { CodeEditorComponent } from '../code-editor/code-editor.component';
import { ContextPaneComponent } from '../context-pane/context-pane.component';

@Component({
  selector: 'app-coding-environment',
  imports: [CodeControlComponent, CodeEditorComponent, ContextPaneComponent],
  templateUrl: './coding-environment.component.html',
  styleUrl: './coding-environment.component.css'
})
export class CodingEnvironmentComponent {

  @Input() height = '';
  @Input() shouldDisplayContext:boolean = false;



  @ViewChild(CodeEditorComponent)Editor!:CodeEditorComponent;
  @ViewChild(CodeControlComponent)ControlPanel!:CodeControlComponent;


  ngAfterViewInit()
  {
    console.log("ran");
    this.ControlPanel.getCode = this.Editor.getCode;
  }
  
}

