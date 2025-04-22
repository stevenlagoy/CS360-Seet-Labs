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
}
