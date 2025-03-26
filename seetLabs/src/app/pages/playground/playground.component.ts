import { Component, ElementRef, Signal, ViewChild, viewChild, viewChildren } from '@angular/core';
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
    JavaOutputComponent
  ],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css'
})
export class PlaygroundComponent 
{

  private _status:string = "Initializing";
  public disabled:boolean = true;


  @ViewChild(JavaOutputComponent)output!:JavaOutputComponent;

  // code mirror
  @ViewChild('codeMirrorInsert') codeMirrorInsert!:ElementRef;
  codeMirrorView! :EditorView;

  get status() {return this._status;}




  ngOnInit()
  {
   
    this.init();

    
 
  }
  
  ngAfterViewInit()
  {
    
    this.codeMirrorView = new EditorView({
      doc: `public class Lab\n{\n\tpublic static void main(String[] args)\n\t{\n\t\tSystem.out.println(\"Hello, world! \\nThis is from java!\");
    }\n}`,
      parent: this.codeMirrorInsert.nativeElement,
      extensions: [basicSetup]
    })
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

    
    cheerpOSAddStringFile("/str/Lab.java", this.codeMirrorView.state.doc.toString());
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
