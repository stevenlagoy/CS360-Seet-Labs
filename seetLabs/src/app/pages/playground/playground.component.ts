import { Component, OnInit, ViewChild, signal} from '@angular/core';

import { GradientHeaderComponent } from '../../components/gradient-header/gradient-header.component';
import { CodingEnvironmentComponent } from '../../java-components/coding-environment/coding-environment.component';
import { LocalStorageService } from '../../services/local-storage.service';


declare var cheerpjInit: any;
declare var cheerpjRunMain: any;
declare var cheerpOSAddStringFile:any;

let instance: PlaygroundComponent | null = null;


@Component({
  selector: 'app-playground',
  imports: [CodingEnvironmentComponent, GradientHeaderComponent
  ],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css'
})
export class PlaygroundComponent implements OnInit
{

  public splashText:string = "";

  @ViewChild(CodingEnvironmentComponent)environment!:CodingEnvironmentComponent;

  constructor(
    private localStorage: LocalStorageService
  ) {
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
    else if(Math.random()<.01)
    {
      this.splashText="Milk: Required, available.";
    }
  }

  ngOnInit(): void {  

    // this.environment.Editor!.setPlaygroundCode();

    const updateCode = setInterval(() => {
      // console.log(this.environment.ControlPanel.getCode());
      this.localStorage.writePlaygroundCode(this.environment.ControlPanel.getCode());
    }, 2000);
  }

}


