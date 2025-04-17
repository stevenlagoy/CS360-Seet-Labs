import { Component, OnInit, inject, signal } from '@angular/core';
import {  MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { GradientHeaderComponent } from '../../components/gradient-header/gradient-header.component';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { first, firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-home-page',
  imports: [CommonModule, GradientHeaderComponent, MatIconModule, MatButtonModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent implements OnInit {

  numActivities = signal<number>(0);
  getDataService = inject(JsonServerTestService);
  moduleNames = signal<String[]>([]);
  moduleLengths = signal<number[]>([]);
  activities = signal<number[][]>([]);

  async ngOnInit(): Promise<void> {
    let modNames: String[] = ["", "", "", "", "", "", "", "", "", "", ""];
    for (let i = 0; i < 11; i++){
      //get content from each module
      const data = this.getDataService.getModuleData(i.toString()).pipe(
      ).subscribe(data => {
        //set names of modules
        modNames[i] = data[0].name;

        //set lengths of modules (# of assignments)
        this.moduleLengths.update(vals => {
          return [...vals, Object.keys(data).length-1];
        })

        //set activity types
        this.activities.update(vals => {
          let activityTypes : number[]  = [];
          for (let j = 0; j < Object.keys(data).length-1; j++){
            activityTypes[j] = data[j+1].type;
          }
          return [...vals, activityTypes];
        })
      })
    }
    this.moduleNames.set(modNames);
  }

  public getModules(num: number): any[] {
    return Array(num);
  }

  public getActivities(num: number): any[] {
    return Array(num);
  }

}
