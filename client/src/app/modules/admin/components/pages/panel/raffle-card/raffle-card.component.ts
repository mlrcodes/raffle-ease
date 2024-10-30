import { Component, Input } from '@angular/core';
import { Raffle } from '../../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-raffle-card',
  standalone: true,
  imports: [],
  templateUrl: './raffle-card.component.html',
  styleUrl: './raffle-card.component.css'
})
export class RaffleCardComponent {

  @Input() raffle!: Raffle;

  

}
