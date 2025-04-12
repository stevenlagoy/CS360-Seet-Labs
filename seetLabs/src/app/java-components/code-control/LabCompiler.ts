import { DomSanitizer } from "@angular/platform-browser";
import { Status } from "./Status";

declare var cheerpjRunMain: any;
declare var cheerpOSAddStringFile:any;

export class LabCompiler
{

    private javaPaths = ["/app/java/LabLauncher.java", "/app/java/JSOutputStream.java"];
    private launcherName:string ="PlaygroundLauncher";
    private status:Status;


    set LauncherName(value:string)
    {
      this.launcherName = value;
    }


    constructor(status:Status, launcherName:string)
    {
        this.status = status;
        this.launcherName = launcherName;
        
        this.javaPaths.push("/app/launchers/"+this.launcherName+".java");


    }

    
    public async compile(code:string):Promise<boolean>
    {
  
      
      
      // paths here 
      let javaFile:string = "/str/Lab.java";
      let classFile:string = "Lab.class";

  
      cheerpOSAddStringFile(javaFile, code);
    
  
      if(!await this.compileJava(javaFile))
      {
        return false;
      }  
  
      if(!await this.CreateExecutableJar(classFile, "/files/Lab.jar"))
      {
        return false;
      }  
      
      return true;
  
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
        this.javaPaths[0], // is there a better way to do this?
        this.javaPaths[1],
        this.javaPaths[2],  
        javaFile
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
      this.status.setStatus( "Making Jar", false);
      let retVal = await cheerpjRunMain(
          "sun.tools.jar.Main",
          "/app/java/tools_modified.jar",
          // args
          "-cf",  "/files/Lab.jar",
          "-C", "/files",
          classFile,
          "LabLauncher.class",
          "JSOutputStream.class",
          this.launcherName+".class"
      );
  
      if(await retVal !== 0)
      {
          this.status.setStatus("Failed to make Jar.", true);
          return false;
      }
  
      return true;
    }

}