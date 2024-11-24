import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ticket } from '../../models/tickets/ticket';
import { GenerateRandomRequest } from '../../models/tickets/reservations/generate-random-request';
import { HttpClient } from '@angular/common/http';
import { SearchRequest } from '../../models/tickets/reservations/search-request';
import { ReservationResponse } from '../../models/tickets/reservations/reservation-response';
import { ReservationRequest } from '../../models/tickets/reservations/reservation-request';

@Injectable({
  providedIn: 'root'
})
export class TicketsService {

  constructor(
    private httpClient: HttpClient
  ) { }


  private baseURL: string = 'http://localhost:8080/api/v1/tickets';

  generateRandom(body: GenerateRandomRequest): Observable<ReservationResponse> {
    return this.httpClient.post(`${this.baseURL}/generate-random`, body) as Observable<ReservationResponse>;
  }

  search(body: SearchRequest): Observable<Ticket[]> {
    return this.httpClient.post(`${this.baseURL}/find-by-number`, body) as Observable<Ticket[]>;
  }

  reserve(body: ReservationRequest): Observable<ReservationResponse> {
    return this.httpClient.post(`${this.baseURL}/reserve`, body) as Observable<ReservationResponse>;
  }

}