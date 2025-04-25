import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsDialogueComponent } from './settings-dialogue.component';

describe('SettingsDialogueComponent', () => {
  let component: SettingsDialogueComponent;
  let fixture: ComponentFixture<SettingsDialogueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingsDialogueComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingsDialogueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
