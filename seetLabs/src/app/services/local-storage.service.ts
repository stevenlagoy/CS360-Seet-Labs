import { Injectable } from '@angular/core';

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
    ]
  };  
  current_data : LooseJsonObject;


  constructor() { 
      if (localStorage.getItem('progress') == null){
        localStorage.setItem('progress', JSON.stringify(this.default_data));
      }
      this.current_data = this.getProgress();
  }

  public writeProgress(module: String, assignment_id: String){
    this.current_data  = this.getProgress();
    this.current_data[Number(module)][Number(assignment_id)-1].passed = "true";
    localStorage.setItem('progress', JSON.stringify(this.current_data));
  }

  public getProgress() : LooseJsonObject {
    return JSON.parse(localStorage.getItem('progress')!);
  }

  public getModulePercentage(module: String): number {
    const moduleSize = Object.keys(this.current_data[Number(module)]).length;
    let totalActivitiesPassed : number = 0;
    for (let i : number = 0; i < moduleSize; i++){
      if (this.current_data[Number(module)][i].passed == "true"){
        totalActivitiesPassed++;
      }
    }
    // console.log(totalActivitiesPassed);
    return Math.round(((totalActivitiesPassed / moduleSize) * 100) * 10) / 10; //round to one decimal
  }
}
  