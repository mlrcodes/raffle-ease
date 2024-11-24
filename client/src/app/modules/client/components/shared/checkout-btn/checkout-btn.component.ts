import { Component, Input, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout-btn',
  standalone: true,
  imports: [],
  templateUrl: './checkout-btn.component.html',
  styleUrl: './checkout-btn.component.css'
})
export class CheckoutBtnComponent {
  @Input() disabled!: boolean;

  constructor(
    private router: Router
  ) {}

  purchase() {
    if (!this.disabled) this.router.navigate(['/client/payment']);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['disabled']) console.log(changes['disabled'].currentValue);
  }
}
