import { Component, inject, signal, OnInit } from '@angular/core';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { catchError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { assignmentType } from '../../models/assignmentType.types';
import template from "./example.html"
import { HttpClient } from '@angular/common/http';
// Import HTML template as raw text using webpack raw-loader
@Component({
  selector: 'app-json-server-test',
  imports: [],
  templateUrl: './json-server-test.component.html',
  styleUrl: './json-server-test.component.scss' // Use inline template
})
export class JsonServerTestComponent implements OnInit {

  constructor(private _route: ActivatedRoute, private http: HttpClient) {}

  getDataService = inject(JsonServerTestService);
  jsonData = signal<assignmentType>({ "id": 0, "type": 0, "file": "reading Material One" });
  template: string = "";

  ngOnInit(): void {
    const id = this._route.snapshot.paramMap.get('id');
    const assignmentNumber = this._route.snapshot.paramMap.get('assignmentNumber');
    this.getDataService.getDataFromAPI(id, assignmentNumber).pipe(
      catchError((err) => {
        console.log(err);
        throw err;
      })
    ).subscribe((data) => {
      this.jsonData.set(data);
      this.template = template;
    });
  }

}