import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartToggleComponent } from './cart-toggle.component';

describe('CartToggleComponent', () => {
  let component: CartToggleComponent;
  let fixture: ComponentFixture<CartToggleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartToggleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CartToggleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
