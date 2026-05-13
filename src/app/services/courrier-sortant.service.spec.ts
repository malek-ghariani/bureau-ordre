import { TestBed } from '@angular/core/testing';

import { CourrierSortantService } from './courrier-sortant.service';

describe('CourrierSortantService', () => {
  let service: CourrierSortantService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourrierSortantService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
