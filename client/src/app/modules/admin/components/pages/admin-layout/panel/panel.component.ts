import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FloatingAddBtnComponent } from './floating-add-btn/floating-add-btn.component';
import { RaffleCardComponent } from './raffle-card/raffle-card.component';
import { ShareRafflesService } from '../../../../../../core/services/raffles/share-raffles.service';
import { StatusCircleComponent } from '../../../shared/status-circle/status-circle.component';
import { ShareImagesService } from '../../../../../../core/services/raffles/share-images.service';
import { Raffle } from '../../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [RouterLink, FloatingAddBtnComponent, RaffleCardComponent, StatusCircleComponent],
  templateUrl: './panel.component.html',
  styleUrl: './panel.component.css'
})
export class PanelComponent {
  constructor(
    private shareRaffles: ShareRafflesService,
    private shareImages: ShareImagesService
  ) { }

  raffles!: Raffle[];
  images!: Map<number, string[]>;

  getRaffles() {
    if (this.shareRaffles.isNull()) {
      this.shareRaffles.rafflesUpdates.subscribe({
        next: (raffles: Map<number, Raffle>) => {
          this.raffles = Array.from(raffles.values());
        }
      });
    } else {
      this.raffles = Array.from(this.shareRaffles.getAll().values());
    }
  }

  getImages() {
    if (this.shareImages.isNull()) {
      this.shareImages.imagesUpdates.subscribe({
        next: (images: Map<number, string[]>) => {
          this.images = images;
        }
      });
    } else {
      this.images = this.shareImages.getAll();
    }
  }

  ngOnInit() {
    this.getRaffles();
    this.getImages();
  }
}
