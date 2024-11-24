import { ChangeDetectorRef, Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { StatusCircleComponent } from '../../../../shared/status-circle/status-circle.component';
import { ShareRafflesService } from '../../../../../../../core/services/raffles/share-raffles.service';
import { Raffle } from '../../../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-raffle-details',
  standalone: true,
  imports: [StatusCircleComponent],
  templateUrl: './raffle-details.component.html',
  styleUrl: './raffle-details.component.css'
})
export class RaffleDetailsComponent {

  constructor(
    private router: Router,
    private shareRaffles: ShareRafflesService,
    private cdr: ChangeDetectorRef
  ) { }

  @Input() raffleId!: number;
  @Input() raffle!: Raffle;
  @Output() raffleChange: EventEmitter<Raffle> = new EventEmitter<Raffle>();

  private getRaffle(): void {
    this.shareRaffles.rafflesUpdates.subscribe({
      next: (raffles: Map<number, Raffle>) => {
        this.setRaffle(raffles);
      }
    });
  }

  private setRaffle(raffles: Map<number, Raffle>) {
    const raffle: Raffle | undefined = raffles.get(this.raffleId);
    if (raffle) {
      this.raffle = raffle;
      this.raffleChange.emit(raffle);
    } else {
      this.redirectToAdmin();
    }
  }

  private redirectToAdmin() {
    this.router.navigate(['/admin']);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['raffleId']) this.getRaffle();
  }
}
