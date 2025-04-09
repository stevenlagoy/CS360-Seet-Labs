import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodingEnvironmentComponent } from './coding-environment.component';

describe('CodingEnvironmentComponent', () => {
  let component: CodingEnvironmentComponent;
  let fixture: ComponentFixture<CodingEnvironmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CodingEnvironmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CodingEnvironmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
