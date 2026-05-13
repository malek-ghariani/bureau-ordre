import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransmissionsRecuesComponent } from './transmissions-recues.component';

describe('TransmissionsRecuesComponent', () => {
  let component: TransmissionsRecuesComponent;
  let fixture: ComponentFixture<TransmissionsRecuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransmissionsRecuesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransmissionsRecuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
