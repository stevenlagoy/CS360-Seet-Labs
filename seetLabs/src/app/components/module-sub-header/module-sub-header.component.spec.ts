import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModuleSubHeaderComponent } from './module-sub-header.component';

describe('ModuleSubHeaderComponent', () => {
  let component: ModuleSubHeaderComponent;
  let fixture: ComponentFixture<ModuleSubHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModuleSubHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModuleSubHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
