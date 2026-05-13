import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransmissionViewComponent } from './transmission-view.component';

describe('TransmissionViewComponent', () => {
  let component: TransmissionViewComponent;
  let fixture: ComponentFixture<TransmissionViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransmissionViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransmissionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
