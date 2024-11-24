import { Component } from '@angular/core';
import { ActivatedRoute, Data, RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../../shared/header/header.component';
import { FooterComponent } from '../../shared/footer/footer.component';
import { ShareRafflesService } from '../../../../../core/services/raffles/share-raffles.service';
import { Raffle } from '../../../../../core/models/raffles/raffle';
import { S3Service } from '../../../../../core/services/s3/s3-service.service';
import { ShareImagesService } from '../../../../../core/services/raffles/share-images.service';
import { forkJoin, map, Observable } from 'rxjs';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {

  constructor(
    private route: ActivatedRoute,
    private shareRaffles: ShareRafflesService,
    private shareImages: ShareImagesService,
    private s3Service: S3Service
  ) { }

  setRaffles() {
    this.route.data.subscribe({
      next: (data: Data) => {
        const raffles: Raffle[] = data['raffles'];
        if (!raffles) return;
        const rafflesMap: Map<number, Raffle> = new Map(raffles.map((raffle: Raffle) => [raffle.id, raffle]));
        this.shareRaffles.updateRaffles(rafflesMap);
        this.setImages(raffles);
      }
    })
  }

  setImages(raffles: Raffle[]) {
    const images: Map<number, string[]> = new Map<number, string[]>();
    const requests: Observable<{ id: number, urls: string[] }>[] = raffles.map((raffle: Raffle) =>
      this.s3Service.getViewUrls(raffle.imageKeys).pipe(
        map((urls: string[]) => ({ id: raffle.id, urls }))
      )
    );

    forkJoin(requests).subscribe({
      next: (results) => {
        results.forEach(({ id, urls }) => {
          images.set(id, urls);
        });
        this.shareImages.updateImages(images);
      }
    });
  }


  ngOnInit() {
    this.setRaffles();
  }

}
