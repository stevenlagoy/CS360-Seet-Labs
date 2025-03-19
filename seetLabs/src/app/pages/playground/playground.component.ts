import { Component, viewChildren } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { GradientHeaderComponent } from '../../components/gradient-header/gradient-header.component';
import { QuizCardComponent } from '../../components/quiz-card/quiz-card.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { ProgressBarComponent } from '../../components/progress-bar/progress-bar.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TextAreaComponent } from '../../components/text-area/text-area.component';
import { InputFieldComponent } from '../../components/input-field/input-field.component';
declare var cheerpjInit: any;
declare var cheerpjRunMain: any;
declare var cheerpOSAddStringFile:any;

@Component({
  selector: 'app-playground',
  imports: [MatButtonModule,
    MatIconModule,
    GradientHeaderComponent,
    QuizCardComponent,
    MatProgressBar,
    MatProgressSpinnerModule,
    ProgressBarComponent,
    MatFormFieldModule,
    MatInputModule,
    InputFieldComponent,
    TextAreaComponent
  ],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css'
})
export class PlaygroundComponent 
{

  private _status:string = "Initializing";
  public disabled:boolean = true;


   codefield= viewChildren(TextAreaComponent)()[0];; 
   output= viewChildren(TextAreaComponent)()[1];

  get status() {return this._status;}


  ngOnInit()
  {
    this.init();
   // this.codefield = 
    //this.output  = 
  }
  

  private async init()
  {
    await cheerpjInit( /*{natives: { Java_JSOutputStream_jsWrite }}*/ );
    let val = await cheerpjRunMain(
        "Main",
        "/app/java/ClearFiles.jar",
    );
    if(await val !== 0)
    {
        alert("Initialization failed.");
        return;
    }
  
    this._status = "Ready";
    this.disabled = false;
 
  } 

  public async compile()
  {
    this.disabled = true;

    this.output!.value = "";
    //document.getElementById("output").innerHTML = "";

    //cheerpOSAddStringFile("/str/Lab.java", document.getElementById("input").value);

    cheerpOSAddStringFile("/str/Lab.java", this.codefield!.value);
     let retVal = 0;

    this._status= "Compiling";
    retVal = await cheerpjRunMain(
      "JavaCLauncher",
      "/app/tools_modified.jar",
      // args
      "-d",
      "/files/",
      "/str/Lab.java",
      "/app/LabLauncher.java",
      "/app/JSOutputStream.java"
    );
    if(await retVal !== 0)
    {
      this._status = "Compilation Failed";
      this.disabled = false;
        return;
    }

    this._status = "Making Jar";
    retVal = await cheerpjRunMain(
        "sun.tools.jar.Main",
        "/app/tools_modified.jar",
        // args
        "-cf",   "/files/Lab.jar",
        "-C", "/files",
        "Lab.class",
        "LabLauncher.class",
        "JSOutputStream.class"
    );

    if(await retVal !== 0)
    {
        this._status= "Failed to make Jar.";
        this.disabled = false;
        return;
    }


    this._status = "Running Program";
    retVal = await cheerpjRunMain(
        "LabLauncher",
        "/files/Lab.jar",
    );


    this._status = "Status: Returned with code "+await retVal;
    this.disabled = false;

  }

}