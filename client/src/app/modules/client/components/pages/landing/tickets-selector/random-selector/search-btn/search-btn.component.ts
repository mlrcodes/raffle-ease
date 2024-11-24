import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TicketsService } from '../../../../../../../../core/services/tickets/tickets.service';
import { ReservationResponse } from '../../../../../../../../core/models/tickets/reservations/reservation-response';
import { ShareOrdersService } from '../../../../../../../../core/services/orders/share-orders.service';
import { Reservation } from '../../../../../../../../core/models/orders/reservation';
import { Ticket } from '../../../../../../../../core/models/tickets/ticket';
import { ShareTicketsService } from '../../../../../../../../core/services/tickets/share-tickets.service';

@Component({
  selector: 'app-search-btn',
  standalone: true,
  imports: [],
  templateUrl: './search-btn.component.html',
  styleUrl: './search-btn.component.css'
})
export class SearchBtnComponent {
  @Input() quantity: number = 0;
  @Input() raffleId!: number;
  @Input() invalid!: boolean;
  @Output() ticketsReserved: EventEmitter<void> = new EventEmitter<void>();

  constructor(
    private ticketsService: TicketsService,
    private shareOrdersService: ShareOrdersService,
    private shareTickets: ShareTicketsService
  ) {}

  reserve() {
    this.ticketsService.generateRandom({
      raffleId: this.raffleId,
      quantity: this.quantity
    }).subscribe({
      next: (reservation: ReservationResponse) => {
        this.shareOrdersService.setReservation(reservation as Reservation);
        this.shareTickets.setTickets(reservation.tickets);
        this.ticketsReserved.emit();
      }
    })
  }
}
