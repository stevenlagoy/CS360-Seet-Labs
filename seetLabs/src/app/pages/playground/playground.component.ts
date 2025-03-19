import { Component } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { GradientHeaderComponent } from '../../components/gradient-header/gradient-header.component';
import { QuizCardComponent } from '../../components/quiz-card/quiz-card.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { ProgressBarComponent } from '../../components/progress-bar/progress-bar.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TextAreaComponent } from '../../components/text-area/text-area.component';
import { InputFieldComponent } from '../../components/input-field/input-field.component';
declare var cheerpjInit: any;
declare var cheerpjRunMain: any;

@Component({
  selector: 'app-playground',
  imports: [MatButtonModule,
    MatIconModule,
    GradientHeaderComponent,
    QuizCardComponent,
    MatProgressBar,
    MatProgressSpinnerModule,
    ProgressBarComponent,
    MatFormFieldModule,
    MatInputModule,
    InputFieldComponent,
    TextAreaComponent
  ],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css'
})
export class PlaygroundComponent 
{

  ngOnInit()
  {
    this.init();

  }

  private async init()
  {
    await cheerpjInit( /*{natives: { Java_JSOutputStream_jsWrite }}*/ );
    let val = await cheerpjRunMain(
        "Main",
        "/app/java/ClearFiles.jar",
    );
    if(await val !== 0)
    {
        alert("Initialization failed.");
        return;
    }
  
    //document.getElementById("compile").removeAttribute("disabled");
    //document.getElementById("status").innerHTML = "Status: Ready";
  } 

}
