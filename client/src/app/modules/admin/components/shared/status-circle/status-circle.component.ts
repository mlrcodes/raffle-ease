import { Component, Input, SimpleChange, SimpleChanges } from '@angular/core';
import { RaffleStatus } from '../../../../../core/models/raffles/raffle';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-status-circle',
  standalone: true,
  imports: [NgClass],
  templateUrl: './status-circle.component.html',
  styleUrl: './status-circle.component.css'
})
export class StatusCircleComponent {

  @Input() status!: RaffleStatus;
  @Input() blackOutline: boolean = false;
}
