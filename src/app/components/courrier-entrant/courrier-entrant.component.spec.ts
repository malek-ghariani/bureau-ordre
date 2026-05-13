import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourrierEntrantComponent } from './courrier-entrant.component';

describe('CourrierEntrantComponent', () => {
  let component: CourrierEntrantComponent;
  let fixture: ComponentFixture<CourrierEntrantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CourrierEntrantComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CourrierEntrantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
