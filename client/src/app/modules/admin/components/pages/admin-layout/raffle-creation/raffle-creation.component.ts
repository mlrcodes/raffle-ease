import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CreationFormComponent } from './creation-form/creation-form.component';
import { RafflesService } from '../../../../../../core/services/raffles/raffles.service';
import { ShareRafflesService } from '../../../../../../core/services/raffles/share-raffles.service';
import { ShareImagesService } from '../../../../../../core/services/raffles/share-images.service';
import { RaffleCreationRequest } from '../../../../../../core/models/raffles/raffle-creation-request';
import { Raffle } from '../../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-raffle-creation',
  standalone: true,
  imports: [CreationFormComponent],
  templateUrl: './raffle-creation.component.html',
  styleUrl: './raffle-creation.component.css'
})
export class RaffleCreationComponent {

  constructor(
    private rafflesService: RafflesService,
    private shareRaffles: ShareRafflesService,
    private shareImages: ShareImagesService,
    private router: Router
  ) {}

  createRaffle(request: RaffleCreationRequest) {
    console.log(request);
    this.rafflesService.create(request).subscribe({
      next: (raffle: Raffle) => {
        this.shareRaffles.setRaffle(raffle);
        this.shareImages.setImages(raffle.id, raffle.imageKeys)
        console.log(raffle)
        this.router.navigate([`/admin/management/${raffle.id}`]);
      }
    })
  }

}
