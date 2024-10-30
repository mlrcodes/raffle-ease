import { Component } from '@angular/core';
import { RaffleFormComponent } from '../../shared/raffle-form/raffle-form.component';
import { RafflesService } from '../../../../../core/services/raffles/raffles.service';
import { RaffleCreationRequest } from '../../../../../core/models/raffles/raffle-creation-request';

@Component({
  selector: 'app-raffle-creation',
  standalone: true,
  imports: [RaffleFormComponent],
  templateUrl: './raffle-creation.component.html',
  styleUrl: './raffle-creation.component.css'
})
export class RaffleCreationComponent {

  constructor(
    private rafflesService: RafflesService
  ) {}

  createRaffle(request: RaffleCreationRequest) {
    this.rafflesService.create(request).subscribe({
      next: (id: string) => {
        console.log(id);
      },
      error: (error: Error) => {
        console.log(error);
      }
    })
  }

}
