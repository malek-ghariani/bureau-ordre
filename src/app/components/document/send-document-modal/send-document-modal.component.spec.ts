import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendDocumentModalComponent } from './send-document-modal.component';

describe('SendDocumentModalComponent', () => {
  let component: SendDocumentModalComponent;
  let fixture: ComponentFixture<SendDocumentModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SendDocumentModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SendDocumentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
