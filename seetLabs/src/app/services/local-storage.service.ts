import { Injectable } from '@angular/core';
import { Buffer } from 'buffer';

type LooseJsonObject = { [key: string]: any };

@Injectable({
  providedIn: 'root'
})

export class LocalStorageService {

  
  default_data : LooseJsonObject = {
    "0": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "1": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "2": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "3": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "4": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "5": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "6": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "7": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 1, "passed": "false" }
    ],
    "8": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "9": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 2, "code": "", "passed": "false" },
      { "id": "3", "type": 1, "passed": "false" }
    ],
    "10": [
      { "id": "1", "type": 0, "passed": "false" },
      { "id": "2", "type": 1, "passed": "false" }
    ],
    "playground": {"code": ""}
  };  
  current_data : LooseJsonObject;


  constructor() { 
      if (localStorage.getItem('progress') == null){
        // console.log("HERE??");
        console.log(Buffer.from("this is a test").toString('base64'));
        localStorage.setItem('progress', Buffer.from(JSON.stringify(this.default_data)).toString('base64'));
      }
      this.current_data = this.getProgress();
  }

  public getActivityStatus(module: String, assignment_id: String) : boolean {
    this.current_data = this.getProgress();
    return this.current_data[Number(module)][Number(assignment_id)-1].passed == "true" ? true : false;
  }

  public writeProgress(module: String, assignment_id: String): void {
    this.current_data  = this.getProgress();
    this.current_data[Number(module)][Number(assignment_id)-1].passed = "true";
    localStorage.setItem('progress', Buffer.from(JSON.stringify(this.current_data)).toString("base64"));
  }

  public writeCode(module: String, assignment_id: String, code: String): void {
    this.current_data  = this.getProgress();
    this.current_data[Number(module)][Number(assignment_id)-1].code = code;
    localStorage.setItem('progress', Buffer.from(JSON.stringify(this.current_data)).toString("base64"));
  }

  public writePlaygroundCode(code: String): void {
    this.current_data  = this.getProgress();
    this.current_data["playground"].code = code;
    localStorage.setItem('progress', Buffer.from(JSON.stringify(this.current_data)).toString("base64"));
  }

  public getPlaygroundCode(): string {
    return this.current_data["playground"].code;
  }

  public codeSaved(module: String, assignment_id: String) : Boolean {
    this.current_data  = this.getProgress();
    return this.current_data[Number(module)][Number(assignment_id)-1].code != "";
  }

  public getCode(module: String, assignment_id: String) : String {
    return this.current_data[Number(module)][Number(assignment_id)-1].code;
  }

  public getProgress() : LooseJsonObject {
    return JSON.parse(Buffer.from(localStorage.getItem('progress')!, 'base64').toString('utf-8'));
  }

  public getModulePercentage(module: String): number {
    const moduleSize = Object.keys(this.current_data[Number(module)]).length;
    let totalActivitiesPassed : number = 0;
    for (let i : number = 0; i < moduleSize; i++){
      if (this.current_data[Number(module)][i].passed == "true"){
        totalActivitiesPassed++;
      }
    }
    return Math.round(((totalActivitiesPassed / moduleSize) * 100) * 10) / 10; //round to one decimal
  }

  public updateKey(newKey: string): void {
    localStorage.setItem('progress', Buffer.from(Buffer.from(newKey, 'base64').toString('utf-8')).toString("base64"));
  }

  public getKey(): String {
    return localStorage.getItem('progress')!;
  }

  public resetKey(): void {
    localStorage.setItem('progress', Buffer.from(JSON.stringify(this.default_data)).toString("base64"));
    this.current_data = this.default_data;
  }

}
  