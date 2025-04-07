import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { CodingEnvironmentComponent } from '../../java-components/coding-environment/coding-environment.component';
import { ModuleSubHeaderComponent } from '../../components/module-sub-header/module-sub-header.component';
import { ActivatedRoute } from '@angular/router';
import hljs from 'highlight.js';
import java from 'highlight.js/lib/languages/java';
import { JsonServerTestService } from '../../services/json-server-test.service';

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
  goForward = signal<boolean>(true);
  goBack = signal<boolean>(true);
  moduleNumber = signal<string>("");
  assignmentNumber = signal<string>("");

  constructor(private _route: ActivatedRoute, private cdr: ChangeDetectorRef) {}
  

  ngOnInit(): void 
  {
    hljs.registerLanguage('java', java);

    const id = this._route.snapshot.paramMap.get('id');
    const assignmentNumber = this._route.snapshot.paramMap.get('assignmentNumber');
    this.moduleNumber.set(id as string);
    this.assignmentNumber.set(assignmentNumber as string);

    this.setNavSignals(assignmentNumber);
    

  }

  setNavSignals(assignmentNumber : any) 
  {
    assignmentNumber = assignmentNumber as number;
    if (assignmentNumber == 1){
      this.goBack.set(false);
    } else if (assignmentNumber == 4){
      this.goForward.set(false);
    }
  }



}
