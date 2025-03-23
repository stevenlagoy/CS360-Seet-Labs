import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { assignmentType } from '../models/assignmentType.types';

@Injectable({
  providedIn: 'root'
})
export class JsonServerTestService {
  
  http = inject(HttpClient)

  getDataFromAPI(id: string | null, assignment: string | null){
    const url = `http://localhost:3000/${id}/${assignment}`;
    return this.http.get<assignmentType>(url, {responseType: 'json'});
  }

}