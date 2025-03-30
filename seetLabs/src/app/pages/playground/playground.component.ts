import { Component} from '@angular/core';

import { CodeEditorComponent } from '../../components/code-editor/code-editor.component';
import { GradientHeaderComponent } from '../../components/gradient-header/gradient-header.component';


declare var cheerpjInit: any;
declare var cheerpjRunMain: any;
declare var cheerpOSAddStringFile:any;

let instance: PlaygroundComponent | null = null;


@Component({
  selector: 'app-playground',
  imports: [CodeEditorComponent, GradientHeaderComponent
  ],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css'
})
export class PlaygroundComponent 
{

 

}


