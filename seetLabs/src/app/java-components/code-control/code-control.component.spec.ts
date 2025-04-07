import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeControlComponent } from './code-control.component';

describe('CodeControlComponent', () => {
  let component: CodeControlComponent;
  let fixture: ComponentFixture<CodeControlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CodeControlComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CodeControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
