import { Component, SimpleChanges } from '@angular/core';
import { EditionFormComponent } from './edition-form/edition-form.component';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { RafflesService } from '../../../../../../core/services/raffles/raffles.service';
import { ShareRafflesService } from '../../../../../../core/services/raffles/share-raffles.service';
import { ShareImagesService } from '../../../../../../core/services/raffles/share-images.service';
import { ShareUrlsService } from '../../../../../../core/services/raffles/share-urls.service';
import { Raffle } from '../../../../../../core/models/raffles/raffle';
import { EditRaffle } from '../../../../../../core/models/raffles/edit-raffle';

@Component({
  selector: 'app-raffle-edition',
  standalone: true,
  imports: [EditionFormComponent],
  templateUrl: './raffle-edition.component.html',
  styleUrl: './raffle-edition.component.css'
})
export class RaffleEditionComponent {

  constructor(
    private rafflesService: RafflesService,
    private shareRaffles: ShareRafflesService,
    private shareImages: ShareImagesService,
    private shareUrls: ShareUrlsService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  raffleId!: number;
  raffle!: Raffle;

  edit(editRaffle: EditRaffle) {
    this.rafflesService.edit(this.raffleId, editRaffle).subscribe({
      next: (raffle: Raffle) => {
        this.shareRaffles.delete(this.raffleId);
        this.shareRaffles.setRaffle(raffle);
        this.shareUrls.updateEvent();
      }
    })
  }

  private getRaffleId(): void {
    this.route.paramMap.subscribe({
      next: (params: ParamMap) => {
        const id: string | null = params.get('id');
        if (id) {
          this.raffleId = this.parseId(id);
          this.raffle = this.shareRaffles.get(this.raffleId)!;
        } else {
          this.shareRaffles.delete(this.raffleId);
          this.shareRaffles.setRaffle(this.raffle);
          this.router.navigate(['/admin']);
        }
      }
    });
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

  private parseId(raffleId: string): number {
    const parsedId = Number.parseFloat(raffleId);
    return isNaN(parsedId) ? 0 : parsedId;
  }

  ngOnInit() {
    this.getRaffleId();
    this.getUrls();
  } 
}
