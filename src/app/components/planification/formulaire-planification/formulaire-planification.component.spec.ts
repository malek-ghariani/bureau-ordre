import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormulairePlanificationComponent } from './formulaire-planification.component';

describe('FormulairePlanificationComponent', () => {
  let component: FormulairePlanificationComponent;
  let fixture: ComponentFixture<FormulairePlanificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormulairePlanificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormulairePlanificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
