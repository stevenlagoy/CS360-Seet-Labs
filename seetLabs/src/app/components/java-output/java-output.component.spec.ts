import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JavaOutputComponent } from './java-output.component';

describe('JavaOutputComponent', () => {
  let component: JavaOutputComponent;
  let fixture: ComponentFixture<JavaOutputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JavaOutputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JavaOutputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
