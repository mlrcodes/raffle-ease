import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { RaffleStatus } from '../../../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-actions',
  standalone: true,
  imports: [],
  templateUrl: './actions.component.html',
  styleUrl: './actions.component.css'
})
export class ActionsComponent {

  constructor(
    private router: Router
  ) {}

  @Input() raffleId!: number;
  @Input() raffleStatus!: RaffleStatus;
  @Output() actionTriggered = new EventEmitter<'publish' | 'pause' | 'restart' | 'delete'>();

  triggerAction(action: 'publish' | 'pause' | 'restart' | 'delete' ) {
    console.log(action)
    this.actionTriggered.emit(action);
  }

  edit() {
    this.router.navigate([`/admin/edit/${this.raffleId}`]);
  }

  isPublished(): boolean {
    return this.raffleStatus !== 'ACTIVE';
  }
}
