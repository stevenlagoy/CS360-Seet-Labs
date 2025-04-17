import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { assignmentType } from '../models/assignmentType.types';
import { firstValueFrom } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class JsonServerTestService {
  
  http = inject(HttpClient)

  getDataFromAPI(id: string | null, assignment: string | null){
    const url = `http://localhost:3000/${id}/${assignment}`;
    return this.http.get<assignmentType>(url, {responseType: 'json'});
  }

  //returns all module data (JSON)
  getModuleData(moduleNumber: string) {
    const url = `http://localhost:3000/${moduleNumber}`;
    return this.http.get<any>(url, {responseType: 'json'}); 
  }
  
  //returns only the number of assignments in that module
  async getModuleContents(moduleNumber: string): Promise<number> {
    const url = `http://localhost:3000/${moduleNumber}`;
    const data = await firstValueFrom(this.http.get(url, {responseType: 'json'}));
    return Object.keys(data).length;
  }
}