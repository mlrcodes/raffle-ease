import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaffleFormComponent } from './raffle-form.component';

describe('RaffleFormComponent', () => {
  let component: RaffleFormComponent;
  let fixture: ComponentFixture<RaffleFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RaffleFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RaffleFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
