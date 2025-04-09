import { ChangeDetectorRef, Component, inject, signal, ViewChild, WritableSignal } from '@angular/core';
import { CodingEnvironmentComponent } from '../../java-components/coding-environment/coding-environment.component';
import { ModuleSubHeaderComponent } from '../../components/module-sub-header/module-sub-header.component';
import { ActivatedRoute } from '@angular/router';
import hljs from 'highlight.js';
import java from 'highlight.js/lib/languages/java';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { catchError } from 'rxjs';
import { CodingActivityData } from '../../models/codingActivityData';

@Component({
  selector: 'app-coding-activity',
  imports: [CodingEnvironmentComponent, ModuleSubHeaderComponent],
  templateUrl: './coding-activity.component.html',
  styleUrl: './coding-activity.component.css'
})
export class CodingActivityComponent 
{

  getDataService = inject(JsonServerTestService);


  // Nav Data Members
  moduleNumber = signal<string>("");
  assignmentNumber = signal<string>("");


  // My gorgeous Children
  @ViewChild(CodingEnvironmentComponent)environment!:CodingEnvironmentComponent;
  jsonData: WritableSignal<CodingActivityData|null> = signal(null);


  constructor(private _route: ActivatedRoute, private cdr: ChangeDetectorRef) {}
  

  ngOnInit(): void 
  {
    hljs.registerLanguage('java', java);

    const id = this._route.snapshot.paramMap.get('id');
    const assignmentNumber = this._route.snapshot.paramMap.get('assignmentNumber');

    this.moduleNumber.set(id as string);
    this.assignmentNumber.set(assignmentNumber as string);

    this.setupContext();
    
  }

  setupContext()
  {

    this.getDataService.getCodingActivityData(this.moduleNumber(), this.assignmentNumber()).pipe(
        catchError((err) => {
          console.log(err);
          throw err;
        })
      ).subscribe(async (data) => {
        this.jsonData.set(data);
  
        console.log("Going to set context pane");
        await this.environment.ContextPane!.loadContext(this.jsonData()!.context);
        console.log("Going to set base code");
        await this.environment.Editor.setBaseCode(this.jsonData()!.base);
      });
    
  }
}
