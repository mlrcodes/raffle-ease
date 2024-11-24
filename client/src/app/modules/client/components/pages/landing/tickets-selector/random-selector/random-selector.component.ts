import { Component, EventEmitter, Input, Output } from '@angular/core';
import { QuantityControlsComponent } from './quantity-controls/quantity-controls.component';
import { Raffle } from '../../../../../../../core/models/raffles/raffle';
import { SearchBtnComponent } from './search-btn/search-btn.component';

@Component({
  selector: 'app-random-selector',
  standalone: true,
  imports: [QuantityControlsComponent, SearchBtnComponent],
  templateUrl: './random-selector.component.html',
  styleUrl: './random-selector.component.css'
})
export class RandomSelectorComponent {
  @Input() raffle!: Raffle;
  @Output() reservationCompleted: EventEmitter<void> = new EventEmitter<void>();
  availableTickets!: number;
  quantity!: number;
  reset!: boolean;
  invalid: boolean = true;

  constructor() {}

  setQuantity(quantity: number) {
    this.quantity = quantity;
  }

  closeReservation() {
    this.reservationCompleted.emit();
    this.reset = true;
    this.invalid = true;
  }

  setInvalid(invalid: boolean) {
    this.invalid = invalid;
  }
}