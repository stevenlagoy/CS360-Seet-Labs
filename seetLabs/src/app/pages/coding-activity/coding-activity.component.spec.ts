import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodingActivityComponent } from './coding-activity.component';

describe('CodingActivityComponent', () => {
  let component: CodingActivityComponent;
  let fixture: ComponentFixture<CodingActivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CodingActivityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CodingActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
