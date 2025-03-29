import { ComponentFixture, TestBed } from '@angular/core/testing';

import { readingActivity } from './reading-activity';

describe('readingActivity', () => {
  let component: readingActivity;
  let fixture: ComponentFixture<readingActivity>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [readingActivity]
    })
    .compileComponents();

    fixture = TestBed.createComponent(readingActivity);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});