import { ChangeDetectorRef, Component, Input, input } from '@angular/core';
import hljs from 'highlight.js';

@Component({
  selector: 'app-context-pane',
  imports: [],
  templateUrl: './context-pane.component.html',
  styleUrl: './context-pane.component.css'
})
export class ContextPaneComponent
{

 
  public template: string = "";


  constructor(private cdr: ChangeDetectorRef) 
  {
    
  }

  public async loadContext(fileName: string) 
  {
    try 
    {
      const template = await import(`./../../../../Data/${fileName}`);
      this.template = template.default;
      this.cdr.detectChanges(); // Force change detection
      hljs.highlightAll();
    } 
    catch (error) 
    {
      console.error("Error loading template: " + error);
    }
  }


}
