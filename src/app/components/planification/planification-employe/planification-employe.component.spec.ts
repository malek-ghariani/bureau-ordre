import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanificationEmployeComponent } from './planification-employe.component';

describe('PlanificationEmployeComponent', () => {
  let component: PlanificationEmployeComponent;
  let fixture: ComponentFixture<PlanificationEmployeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlanificationEmployeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanificationEmployeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
