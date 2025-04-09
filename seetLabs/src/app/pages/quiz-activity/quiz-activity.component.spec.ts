import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizActivityComponent } from './quiz-activity.component';

describe('QuizActivityComponent', () => {
  let component: QuizActivityComponent;
  let fixture: ComponentFixture<QuizActivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizActivityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuizActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
