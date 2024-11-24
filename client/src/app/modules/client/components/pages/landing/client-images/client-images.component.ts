import { Component, Input, SimpleChanges } from '@angular/core';
import { ThumbnailsComponent } from "./thumbnails/thumbnails.component";
import { S3Service } from '../../../../../../core/services/s3/s3-service.service';

@Component({
  selector: 'app-client-images',
  standalone: true,
  imports: [ThumbnailsComponent],
  templateUrl: './client-images.component.html',
  styleUrl: './client-images.component.css'
})
export class ClientImagesComponent {
  @Input() imageKeys!: string[];
  urls!: string[];
  mainImage!: string;

  constructor(
    private s3Service: S3Service
  ) {}
  
  onImageSelected(image: string): void {
    this.mainImage = image;
  }

  getViewUrls(keys: string[]) {
    this.s3Service.getViewUrls(keys).subscribe({
      next: (urls: string[]) => {
        this.urls = urls;
        this.mainImage = urls[0];
      }
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['imageKeys']) {
      this.getViewUrls(this.imageKeys);
    }
  }
}
