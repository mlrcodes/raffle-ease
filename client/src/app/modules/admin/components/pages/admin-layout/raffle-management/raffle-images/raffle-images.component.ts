import { Component, Input, SimpleChanges } from '@angular/core';
import { ShareImagesService } from '../../../../../../../core/services/raffles/share-images.service';

@Component({
  selector: 'app-raffle-images',
  standalone: true,
  imports: [],
  templateUrl: './raffle-images.component.html',
  styleUrl: './raffle-images.component.css'
})
export class RaffleImagesComponent {

  constructor(
    private shareImages: ShareImagesService
  ) { }

  @Input() raffleId!: number;
  images!: string[];
  activeIndex: number = 0;

  get activeImage() {
    return this.images[this.activeIndex];
  }

  setActiveImage(index: number) {
    this.activeIndex = index;
  }

  private getImages(): void {
    if (this.shareImages.isNull()) {
      this.shareImages.imagesUpdates.subscribe({
        next: (imagesMap: Map<number, string[]>) => {
          this.setImages(imagesMap);
        }
      });
    } else {
      this.setImages(this.shareImages.getAll());
    }
  }

  private setImages(imagesMap: Map<number, string[]>) {
    const images: string[] | undefined = imagesMap.get(this.raffleId);
    if (images) this.images = images;
    console.log(this.images)
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['raffleId']) this.getImages();    
  }
}

