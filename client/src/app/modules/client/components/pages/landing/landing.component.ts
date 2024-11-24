import { Component } from '@angular/core';
import { ActivatedRoute, Data } from '@angular/router';
import { Raffle } from '../../../../../core/models/raffles/raffle';
import { RaffleDetailsComponent } from "../../../../admin/components/pages/admin-layout/raffle-management/raffle-details/raffle-details.component";
import { RaffleDescriptionComponent } from "./raffle-description/raffle-description.component";
import { ShareOrdersService } from '../../../../../core/services/orders/share-orders.service';
import { TicketsSelectorComponent } from './tickets-selector/tickets-selector.component';
import { ClientImagesComponent } from './client-images/client-images.component';
import { CartComponent } from './cart/cart.component';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [ClientImagesComponent, RaffleDetailsComponent, RaffleDescriptionComponent, TicketsSelectorComponent, CartComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent {
  raffle!: Raffle;

  constructor(
    private route: ActivatedRoute,
    private shareOrdersService: ShareOrdersService
  ) {}

  setRaffle() {
    this.route.data.subscribe({
      next: (data: Data) => {
        const raffle: Raffle = data['raffle'];
        if (!raffle) return;
        this.raffle = raffle;
        this.shareOrdersService.initOrderRequest(raffle.id);
      }
    });
  }

  ngOnInit() {
    this.setRaffle();
  }
}
