import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradientHeaderComponent } from './gradient-header.component';

describe('GradientHeaderComponent', () => {
  let component: GradientHeaderComponent;
  let fixture: ComponentFixture<GradientHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradientHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GradientHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
