import { Component, inject, signal, OnInit, ChangeDetectorRef } from '@angular/core';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { catchError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { assignmentType } from '../../models/assignmentType.types';
import hljs from 'highlight.js/lib/core';
import java from 'highlight.js/lib/languages/java';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-json-server-test',
  imports: [CommonModule],
  templateUrl: './json-server-test.component.html',
  styleUrl: './json-server-test.component.scss' // Use inline template
})
export class JsonServerTestComponent implements OnInit {

  constructor(private _route: ActivatedRoute, private cdr: ChangeDetectorRef) {}
  
  getDataService = inject(JsonServerTestService);
  jsonData = signal<assignmentType>({ "id": 0, "type": 0, "file": "reading Material One" });
  template: string = "";

  ngOnInit(): void {
    hljs.registerLanguage('java', java);
    const id = this._route.snapshot.paramMap.get('id');
    const assignmentNumber = this._route.snapshot.paramMap.get('assignmentNumber');
    this.getDataService.getDataFromAPI(id, assignmentNumber).pipe(
      catchError((err) => {
        console.log(err);
        throw err;
      })
    ).subscribe(async (data) => {
      this.jsonData.set(data);
      await this.loadReading(data.file);
    });
  }


  async loadReading(fileName: string) {
    try {
      const template = await import(`./../../../../Data/${fileName}`);
      this.template = template.default;
      this.cdr.detectChanges(); // Force change detection
      hljs.highlightAll();
    } catch (error) {
      console.error("Error loading template: " + error);
    }
  }

}