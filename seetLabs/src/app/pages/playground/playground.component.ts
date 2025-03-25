import { Component, Signal, ViewChild, viewChild, viewChildren } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TextAreaComponent } from '../../components/text-area/text-area.component';
import {Java_JSOutputStream_jsWrite, JavaOutputComponent} from '../../components/java-output/java-output.component';
import {basicSetup} from "codemirror";
import {EditorView} from "@codemirror/view";

declare var cheerpjInit: any;
declare var cheerpjRunMain: any;
declare var cheerpOSAddStringFile:any;

let instance: PlaygroundComponent | null = null;


@Component({
  selector: 'app-playground',
  imports: [MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    TextAreaComponent,
    JavaOutputComponent
  ],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css'
})
export class PlaygroundComponent 
{

  private _status:string = "Initializing";
  public disabled:boolean = true;


 
  @ViewChild(TextAreaComponent) input!:TextAreaComponent;
 
  @ViewChild(JavaOutputComponent)output!:JavaOutputComponent;

  get status() {return this._status;}




  ngOnInit()
  {
   
    this.init();
 
  }
  

  private async init()
  {
    await cheerpjInit( {natives: { Java_JSOutputStream_jsWrite }}  );
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

    this.output!.clear();

    console.log(this.input!.content);
    cheerpOSAddStringFile("/str/Lab.java", this.input!.content);
    let retVal = 0;

    this._status= "Compiling";
    retVal = await cheerpjRunMain(
      "JavaCLauncher",
      "/app/java/tools_modified.jar",
      // args
      "-d",
      "/files/",
      "/str/Lab.java",
      "/app/java/LabLauncher.java",
      "/app/java/JSOutputStream.java"
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
        "/app/java/tools_modified.jar",
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


    this._status = "Returned with code "+await retVal;
    this.disabled = false;

  }

}
