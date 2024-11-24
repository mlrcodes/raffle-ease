import { Component } from '@angular/core';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-cart-toggle',
  standalone: true,
  imports: [],
  templateUrl: './cart-toggle.component.html',
  styleUrls: ['./cart-toggle.component.css'],
})
export class CartToggleComponent {
  constructor(private cartService: CartService) { }

  get isCartVisible(): boolean {
    return this.cartService.isCartVisible;
  }

  toggleCart() {
    this.cartService.toggleCart();
  }
}
