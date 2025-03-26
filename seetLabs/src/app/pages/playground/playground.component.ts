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
  public compileButtonDisabled:boolean = true;


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
      // starting code. Garbage format, I know, but temporary (hopefully).
      doc: `public class Lab\n{\n\tpublic static void main(String[] args)\n\t{\n\t\tSystem.out.println(\"Hello, world! \\nThis is from java!\");
    }\n}`,
      parent: this.codeMirrorInsert.nativeElement,
      extensions: [basicSetup]
    })
  }



  private async init()
  {
    // actually init cheerpj.
    await cheerpjInit( {natives: { Java_JSOutputStream_jsWrite }}  );

    // this just deletes files from previous code compilations. It's not strictly needed, really.
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
    this.compileButtonDisabled = false;
 
  } 

  public async compile()
  {

    this.compileButtonDisabled = true;
    this.output!.clear();  
    
    // paths here 
    let javaFile:String = "/str/Lab.java";
    let classFile:String = "Lab.class";
    let jarFile:String = "/files/Lab.jar";

    cheerpOSAddStringFile(javaFile, this.codeMirrorView.state.doc.toString());
  

    if(!await this.compileJava(javaFile))
    {
      this.compileButtonDisabled = false;
      return;
    }  

    if(!await this.CreateExecutableJar(classFile, jarFile))
    {
      this.compileButtonDisabled = false;
      return;
    }  

    this._status = "Running Program";
    let retVal = await cheerpjRunMain(
        "LabLauncher",
        jarFile,
    );


    this._status = "Returned with code "+await retVal;
    this.compileButtonDisabled = false;

  }


  private async compileJava(javaFile:String) : Promise<boolean>
  {
    this._status= "Compiling";
    let retVal = await cheerpjRunMain(
      "JavaCLauncher",
      "/app/java/tools_modified.jar",
      // args
      "-d",
      "/files/",
      javaFile,
      "/app/java/LabLauncher.java",
      "/app/java/JSOutputStream.java"
    );
    if(await retVal !== 0)
    {
      this._status = "Compilation Failed";
      return false;
    }

    return true;
  }

  // creates a jar file from clas files.
  private async CreateExecutableJar(classFile:String, jarFile:String) : Promise<boolean>
  {
    this._status = "Making Jar";
    let retVal = await cheerpjRunMain(
        "sun.tools.jar.Main",
        "/app/java/tools_modified.jar",
        // args
        "-cf",  "/files/Lab.jar",
        "-C", "/files",
        classFile,
        "LabLauncher.class",
        "JSOutputStream.class"
    );

    if(await retVal !== 0)
    {
        this._status= "Failed to make Jar.";
        return false;
    }

    return true;
  }

}
