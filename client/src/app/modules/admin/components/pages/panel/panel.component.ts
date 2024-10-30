import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FloatingAddBtnComponent } from './floating-add-btn/floating-add-btn.component';
import { RaffleCardComponent } from './raffle-card/raffle-card.component';
import { Raffle } from '../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [RouterLink, FloatingAddBtnComponent, RaffleCardComponent],
  templateUrl: './panel.component.html',
  styleUrl: './panel.component.css'
})
export class PanelComponent {

  constructor() { }

  raffles!: Raffle[];
}
