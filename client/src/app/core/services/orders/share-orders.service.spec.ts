import { TestBed } from '@angular/core/testing';

import { ShareOrdersService } from './share-orders.service';

describe('ShareOrdersService', () => {
  let service: ShareOrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShareOrdersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
