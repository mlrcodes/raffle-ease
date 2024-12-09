import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Raffle } from '../../../../../../../core/models/raffles/raffle';
import { TicketsService } from '../../../../../../../core/services/tickets/tickets.service';
import { Ticket } from '../../../../../../../core/models/tickets/ticket';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { catchError, debounceTime, distinctUntilChanged, of, switchMap } from 'rxjs';
import { SearchResultsComponent } from "./search-results/search-results.component";
import { SelectBtnComponent } from "./select-btn/select-btn.component";
import { NgClass } from '@angular/common';
import { ShareTicketsService } from '../../../../../../../core/services/tickets/share-tickets.service';

@Component({
  selector: 'app-tickets-searcher',
  standalone: true,
  imports: [SearchResultsComponent, SelectBtnComponent, ReactiveFormsModule, NgClass],
  templateUrl: './tickets-searcher.component.html',
  styleUrl: './tickets-searcher.component.css'
})
export class TicketsSearcherComponent {
  constructor(
    private ticketsService: TicketsService
  ) { }

  @Input() raffle!: Raffle;
  @Output() reservationCompleted: EventEmitter<void> = new EventEmitter<void>();
  selectedTicket!: Ticket | null;
  searchControl = new FormControl();
  searchResults!: Ticket[];
  invalidInput: boolean = true;
  display: boolean = false;

  search(): void {
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((searchText: string) => {
        if (this.isInvalid(searchText)) {
          this.invalidInput = true;
          this.display = false,
          this.searchResults = [];
          return [];
        } else {
          return this.ticketsService.search({
            raffleId: this.raffle.id,
            ticketNumber: searchText
          }).pipe(
            catchError(() => {
              return of([]);
            })
          );
        }
      })
    ).subscribe({
      next: (tickets: Ticket[]) => {
        this.display = tickets.length ? true : false;
        this.invalidInput = tickets.length ? false : true;
        this.searchResults = tickets;
      }
    });
  }

  selectTicket(ticket: Ticket): void {
    this.searchControl.setValue(ticket.ticketNumber, { emitEvent: false });
    this.searchResults = [];
    this.selectedTicket = ticket;
    console.log(this.selectTicket)
  }

  closeReservation() {
    this.searchControl.setValue("", { emitEvent: false });
    this.selectedTicket = null;
    this.invalidInput = true;
    this.display = false;
    this.reservationCompleted.emit();
  }

  isInvalid(searchText: string): boolean {
    return searchText === null ||
      searchText.length < 1 ||
      !searchText.trim() ||
      !/^\d*$/.test(searchText)
  }

  ngOnInit() {
    this.search();
  }
}