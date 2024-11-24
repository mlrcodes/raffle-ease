import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CreationFormComponent } from './creation-form/creation-form.component';
import { RafflesService } from '../../../../../../core/services/raffles/raffles.service';
import { ShareRafflesService } from '../../../../../../core/services/raffles/share-raffles.service';
import { ShareImagesService } from '../../../../../../core/services/raffles/share-images.service';
import { RaffleCreationRequest } from '../../../../../../core/models/raffles/raffle-creation-request';
import { Raffle } from '../../../../../../core/models/raffles/raffle';
import { ShareUrlsService } from '../../../../../../core/services/raffles/share-urls.service';

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
    private shareUrls: ShareUrlsService,
    private router: Router
  ) {}

  raffleId!: number;

  createRaffle(request: RaffleCreationRequest) {
    this.rafflesService.create(request).subscribe({
      next: (raffle: Raffle) => {
        this.shareRaffles.setRaffle(raffle);
        this.raffleId = raffle.id;
        this.shareUrls.updateEvent();
      }
    })
  }

  private getUrls() {
    this.shareUrls.urlsUpdates.subscribe({
      next: (urls: string[]) => {
        this.shareImages.delete(this.raffleId);
        this.shareImages.setImages(this.raffleId, urls);
        this.router.navigate([`/admin/management/${this.raffleId}`]);
      }
    })
  }

  ngOnInit() {
    this.getUrls();
  }
}
