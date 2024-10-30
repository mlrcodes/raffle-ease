import { TestBed } from '@angular/core/testing';

import { RafflesService } from './raffles.service';

describe('RafflesService', () => {
  let service: RafflesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RafflesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
