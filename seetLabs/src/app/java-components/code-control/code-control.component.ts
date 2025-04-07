import { Component, Input, signal, Signal, ViewChild } from '@angular/core';
import { Java_JSOutputStream_jsWrite, JavaOutputComponent } from '../java-output/java-output.component';
import { MatButtonModule } from '@angular/material/button';
import { Status } from './Status';


declare var cheerpjInit: any;
declare var cheerpjRunMain: any;
declare var cheerpOSAddStringFile:any;

let LabLauncher:any = null;
export async function Java_LabLauncher_callJS(lib:any /*: CJ3Library*/,self:any /*java object???*/)
{
  LabLauncher = self;
  return new Promise(() => {}); // Keeps the function from returning
}




@Component({
  selector: 'app-code-control',
  imports: [JavaOutputComponent, MatButtonModule],
  templateUrl: './code-control.component.html',
  styleUrl: './code-control.component.css'
})
export class CodeControlComponent 
{

    private _status:Status = new Status();
    get status() {return this._status;}

    @ViewChild(JavaOutputComponent)output!:JavaOutputComponent;


    public getCode!:()=>string;

    ngOnInit()
    {
     
      this.init();
    }

    private async init()
    {
      // actually init cheerpj.
      await cheerpjInit( {natives: { Java_JSOutputStream_jsWrite, Java_LabLauncher_callJS }}  );
  
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
    
      this._status.setStatus("", true, true);
    
    } 


    
  public async mainButton()
  {
    if(!this._status.programRunning)
    {
      this.compile();
      return;
    }

    // stop the program.
    this._status.programRunning = false;
    this._status.setStatus("Program Stopped", true);
    LabLauncher?.end();
  }


  public async compile()
  {

    this.output!.clear();  
    
    // paths here 
    let javaFile:String = "/str/Lab.java";
    let classFile:String = "Lab.class";
    let jarFile:String = "/files/Lab.jar";

    cheerpOSAddStringFile(javaFile, this.getCode());
  

    if(!await this.compileJava(javaFile))
    {
      return;
    }  

    if(!await this.CreateExecutableJar(classFile, jarFile))
    {
      return;
    }  

    this._status.setStatus("Stop Program", false, true);
    this._status.programRunning = true;
    let retVal = await cheerpjRunMain(
        "LabLauncher",
        jarFile,
    );
    this._status.programRunning = false;

    this._status.setStatus("Returned with code "+await retVal, true);

  }


  private async compileJava(javaFile:String) : Promise<boolean>
  {
   
    this.status.setStatus("Compiling", false);
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
      this.status.setStatus("Compilation Failed", true);
      return false;
    }

    return true;
  }

  // creates a jar file from clas files.
  private async CreateExecutableJar(classFile:String, jarFile:String) : Promise<boolean>
  {
    this._status.setStatus( "Making Jar", false);
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
        this._status.setStatus("Failed to make Jar.", true);
        return false;
    }

    return true;
  }
}
