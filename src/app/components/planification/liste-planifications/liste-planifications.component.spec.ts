import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListePlanificationsComponent } from './liste-planifications.component';

describe('ListePlanificationsComponent', () => {
  let component: ListePlanificationsComponent;
  let fixture: ComponentFixture<ListePlanificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListePlanificationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListePlanificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
