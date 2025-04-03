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

  public splashText:string = "";

  constructor()
  {
    let splashTexts 
    = ["What will you create?", "Infinite Possibilities.", 
      "Go on, play!", "Don't let the bugs get you down.", 
      "You can make anything you can imagine.",
       "Hmm. I wonder what this does...", "Remember to write your Javadoc(tm)!"]


    this.splashText = splashTexts[Math.floor(Math.random()*splashTexts.length)];

    if(Math.random()<.02)
    {
      this.splashText="Be careful. Mr. Seet is watching.";
    }
  }
 

}


