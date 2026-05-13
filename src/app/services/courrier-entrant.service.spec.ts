import { TestBed } from '@angular/core/testing';

import { CourrierEntrantService } from './courrier-entrant.service';

describe('CourrierEntrantService', () => {
  let service: CourrierEntrantService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourrierEntrantService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
