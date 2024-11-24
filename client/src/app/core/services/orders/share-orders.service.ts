import { Injectable } from '@angular/core';
import { BehaviorSubject, filter, Observable, Subject } from 'rxjs';
import { OrderRequest } from '../../models/orders/order-request';
import { Reservation } from '../../models/orders/reservation';

@Injectable({
  providedIn: 'root'
})
export class ShareOrdersService {
  private orderRequest!: OrderRequest;

  public initOrderRequest(raffleId: number): void {
    this.orderRequest = {
      raffleId,
      reservations: []
    }
  }

  public getOrder(): OrderRequest {
    return this.orderRequest;
  }

  public setReservation(reservation: Reservation): void {
    this.orderRequest.reservations.push(reservation);
  }
}
