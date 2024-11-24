import { Injectable } from '@angular/core';
import { Ticket } from '../../../../../../core/models/tickets/ticket';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private tickets: Ticket[] = [];
  isCartVisible = false;
  private isBtnDisabledSource = new BehaviorSubject<boolean>(true); 
  isBtnDisabled$: Observable<boolean> = this.isBtnDisabledSource.asObservable();

  getTickets() {
    return [...this.tickets]; 
  }

  addTickets(newTickets: Ticket[]) {
    console.log("TICKETS ADDED")
    this.tickets.push(...newTickets);
    this.updateCheckoutBtnState(); 
  }

  removeTicket(ticket: Ticket) {
    this.tickets = this.tickets.filter((t: Ticket) => t.id !== ticket.id);
    this.updateCheckoutBtnState(); 
  }

  getTotal() {
    return this.tickets.reduce((sum, ticket: Ticket) => sum + ticket.price, 0);
  }  

  toggleCart() {
    this.isCartVisible = !this.isCartVisible;
  }

  setTickets(tickets: Ticket[]) {
    this.tickets = tickets;
  }

  updateCheckoutBtnState() {
    const isDisabled: boolean = !(this.tickets.length > 0) || !(this.getTotal() > 0);
    this.isBtnDisabledSource.next(isDisabled);
  }
}
