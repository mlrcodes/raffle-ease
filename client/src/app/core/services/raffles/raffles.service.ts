import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RaffleCreationRequest } from '../../models/raffles/raffle-creation-request';
import { Observable } from 'rxjs';
import { Raffle } from '../../models/raffles/raffle';
import { EditRaffle } from '../../models/raffles/edit-raffle';

@Injectable({
  providedIn: 'root'
})
export class RafflesService {

  constructor(
    private httpClient: HttpClient
  ) { }

  private baseUrl: string = 'http://localhost:8080/api/v1/raffles';

  create(request: RaffleCreationRequest): Observable<Raffle> {
    return this.httpClient.post(`${this.baseUrl}/create`, request) as Observable<Raffle>;
  }

  get(id: number): Observable<Raffle> {
    return this.httpClient.get(`${this.baseUrl}/get/${id}`) as Observable<Raffle>;
  }

  getAll(): Observable<Raffle[]> {
    return this.httpClient.get(`${this.baseUrl}/get-all/`) as Observable<Raffle[]>;
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/delete/${id}`) as Observable<void>;
  }

  publish(id: number): Observable<Raffle> {
    return this.httpClient.put(`${this.baseUrl}/publish/${id}`, {}) as Observable<Raffle>;
  }

  pause(id: number): Observable<Raffle> {
    return this.httpClient.put(`${this.baseUrl}/pause/${id}`, {}) as Observable<Raffle>;
  }

  restart(id: number): Observable<Raffle> {
    return this.httpClient.put(`${this.baseUrl}/restart/${id}`, {}) as Observable<Raffle>;
  }

  edit(raffleId: number, editRaffle: EditRaffle){
    return this.httpClient.put(`${this.baseUrl}/edit/${raffleId}`, editRaffle) as Observable<Raffle>;
  }
}
