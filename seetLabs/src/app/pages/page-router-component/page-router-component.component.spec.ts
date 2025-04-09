import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageRouterComponentComponent } from './page-router-component.component';

describe('PageRouterComponentComponent', () => {
  let component: PageRouterComponentComponent;
  let fixture: ComponentFixture<PageRouterComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageRouterComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageRouterComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
