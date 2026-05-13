import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponsableLayoutComponent } from './responsable-layout.component';

describe('ResponsableLayoutComponent', () => {
  let component: ResponsableLayoutComponent;
  let fixture: ComponentFixture<ResponsableLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResponsableLayoutComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponsableLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
