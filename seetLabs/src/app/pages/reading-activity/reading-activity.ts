import { Component, inject, signal, OnInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { JsonServerTestService } from '../../services/json-server-test.service';
import { catchError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { assignmentType } from '../../models/assignmentType.types';
import hljs from 'highlight.js/lib/core';
import java from 'highlight.js/lib/languages/java';
import { CommonModule } from '@angular/common';
import { ModuleSubHeaderComponent } from '../../components/module-sub-header/module-sub-header.component';
import { LocalStorageService } from '../../services/local-storage.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from '../../components/snackbar/snackbar.component';
@Component({
  selector: 'readingActivity',
  imports: [CommonModule, ModuleSubHeaderComponent],
  templateUrl: './readingActivity.component.html',
  styleUrl: './readingActivity.component.scss' // Use inline template
})
export class readingActivity implements OnInit {

  constructor(private _route: ActivatedRoute, private cdr: ChangeDetectorRef) {}
  
  getDataService = inject(JsonServerTestService);
  jsonData = signal<assignmentType>({ "id": 0, "type": 0, "file": "reading Material One" });
  template: string = "";
  localStorage = new LocalStorageService();
  moduleProgress = signal<number>(0);
  activityPassed = signal<boolean>(false);

  // Nav Data Members
  moduleNumber = signal<string>("");
  assignmentNumber = signal<string>("");

  private snackBar = inject(MatSnackBar);

  ngOnInit(): void {
    hljs.registerLanguage('java', java);
    const id = this._route.snapshot.paramMap.get('id');
    const assignmentNumber = this._route.snapshot.paramMap.get('assignmentNumber');
    this.moduleNumber.set(id as string);
    this.assignmentNumber.set(assignmentNumber as string);
    this.moduleProgress.set(this.localStorage.getModulePercentage(this.moduleNumber()));
    this.activityPassed.set(this.localStorage.getActivityStatus(this.moduleNumber(), this.assignmentNumber()));
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

  //check if the user has "read" the entire page
  @HostListener("window:scroll", ['$event'])
  hasFinished(event: Event): void {
    const bottom_nav = document.getElementById('bottom_nav');
    const {top, left, bottom, right }= bottom_nav!.getBoundingClientRect();
    
    //user scrolled to bottom
    if (top >= 0 && left >= 0 && bottom <= (window.innerHeight) && right <= (window.innerWidth)){
      this.localStorage.writeProgress(this.moduleNumber(), this.assignmentNumber());
      this.localStorage.getModulePercentage(this.moduleNumber());
      this.moduleProgress.set(this.localStorage.getModulePercentage(this.moduleNumber()));
      if (this.activityPassed() == false){
        this.openSnackBar();
        this.activityPassed.set(true);
      }
    }
  }

  openSnackBar(): void {
    this.snackBar.openFromComponent(SnackbarComponent, {
      duration: 2000
    });
  }


}