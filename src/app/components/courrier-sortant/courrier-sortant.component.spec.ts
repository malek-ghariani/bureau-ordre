import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourrierSortantComponent } from './courrier-sortant.component';

describe('CourrierSortantComponent', () => {
  let component: CourrierSortantComponent;
  let fixture: ComponentFixture<CourrierSortantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CourrierSortantComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CourrierSortantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
