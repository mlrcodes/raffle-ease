import { Component } from '@angular/core';
import { CartToggleComponent } from './cart-toggle/cart-toggle.component';
import { OverlayComponent } from './overlay/overlay.component';
import { CartService } from './cart.service';
import { Ticket } from '../../../../../../core/models/tickets/ticket';
import { ShareTicketsService } from '../../../../../../core/services/tickets/share-tickets.service';
import { CartItemComponent } from './cart-item/cart-item.component';
import { CheckoutBtnComponent } from "../../../shared/checkout-btn/checkout-btn.component";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CartToggleComponent, OverlayComponent, CartItemComponent, CheckoutBtnComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  tickets: Ticket[] = [];
  isBtnDisabled: boolean = true;

  constructor(
    private shareTickets: ShareTicketsService,
    private cartService: CartService
  ) { }

  getTickets() {
    this.shareTickets.tickets.subscribe({
      next: (tickets: Ticket[]) => {
        this.cartService.addTickets(tickets);
        this.tickets = this.cartService.getTickets();
      }
    });
  }

  get total() {
    return this.cartService.getTotal();
  }

  get isCartVisible() {
    return this.cartService.isCartVisible;
  }

  checkIfDisabled() {
    this.cartService.isBtnDisabled$.subscribe({
      next: (isDisabled: boolean) => {
        console.log("STATE CHANGED: " + isDisabled)
        this.isBtnDisabled = isDisabled;
      }
    });
  }

  toggleCart() {
    this.cartService.toggleCart();
  }

  ngOnInit() {
    this.getTickets();
    this.checkIfDisabled();
  }
}
