import { Component, Input, SimpleChange, SimpleChanges } from '@angular/core';
import { CartService } from '../cart.service';
import { Ticket } from '../../../../../../../core/models/tickets/ticket';

@Component({
  selector: 'app-cart-item',
  standalone: true, 
  imports: [],
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css'],
})
export class CartItemComponent {
  @Input() ticket!: Ticket;

  constructor(
    private cartService: CartService
  ) {}

  removeItem() {
    this.cartService.removeTicket(this.ticket);
  }
}
