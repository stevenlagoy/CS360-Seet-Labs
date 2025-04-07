import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContextPaneComponent } from './context-pane.component';

describe('ContextPaneComponent', () => {
  let component: ContextPaneComponent;
  let fixture: ComponentFixture<ContextPaneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContextPaneComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContextPaneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
