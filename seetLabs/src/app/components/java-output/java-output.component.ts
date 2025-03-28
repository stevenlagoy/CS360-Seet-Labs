import { Component } from '@angular/core';


let instance: JavaOutputComponent|null = null;

@Component({
  selector: 'app-java-output',
  imports: [],
  templateUrl: './java-output.component.html',
  styleUrl: './java-output.component.css'
})
export class JavaOutputComponent 
{

  public content:string = "";
  private errorOpen:boolean = false;

  ngOnInit()
  {
    if(instance != null)
    {
      throw "Only one java-output-component should exist?";
    }

    instance = this;
  }

  public clear():void{
    this.content = "";
  }

  public append(str:string)
  {
    this.errorOpen = false;
    this.content+=str;
  }

  public appendError(str:string)
  {

  
    let text = this.content;
    if(this.errorOpen)
    {
      // remove the ending tag
      text = 
        this.content.substring(0,this.content.length-"</span>".length);
    }
    else
    {
      text += "<span class=\"error\">";
    }

    this.errorOpen=true;
    text+=str;
    text+="</span>";

    this.content = text;
  }
}


export async function Java_JSOutputStream_jsWrite(lib:any /*: CJ3Library*/,self:any /*java object???*/, b:number /*number, probably*/, error:boolean)
{
  if(instance === null)
  {
    console.log("Couldn't find Java output component!");
    return;
  }
  
  if(error)
  {
    instance!.appendError( String.fromCharCode(b))
    return
  }

  instance!.append( String.fromCharCode(b))

}
