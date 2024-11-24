import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TicketsService } from '../../../../../../../../core/services/tickets/tickets.service';
import { ShareOrdersService } from '../../../../../../../../core/services/orders/share-orders.service';
import { ReservationResponse } from '../../../../../../../../core/models/tickets/reservations/reservation-response';
import { Reservation } from '../../../../../../../../core/models/orders/reservation';
import { Ticket } from '../../../../../../../../core/models/tickets/ticket';
import { ShareTicketsService } from '../../../../../../../../core/services/tickets/share-tickets.service';

@Component({
  selector: 'app-select-btn',
  standalone: true,
  imports: [],
  templateUrl: './select-btn.component.html',
  styleUrl: './select-btn.component.css'
})
export class SelectBtnComponent {
  @Input() raffleId!: number;
  @Input() ticket!: Ticket | null;
  @Input() invalid!: boolean;
  @Output() ticketReserved: EventEmitter<void> = new EventEmitter<void>();
  
  constructor(
    private ticketsService: TicketsService,
    private shareOrdersService: ShareOrdersService,
    private shareTickets: ShareTicketsService
  ) { }

  reserve() {
    this.ticketsService.reserve({
      raffleId: this.raffleId,
      ticketsIds: [this.ticket!.id]
    }).subscribe({
      next: (reservation: ReservationResponse) => {
        this.shareOrdersService.setReservation(reservation as Reservation);
        this.shareTickets.setTickets(reservation.tickets);
        this.ticketReserved.emit();
      }
    })
  }
}
