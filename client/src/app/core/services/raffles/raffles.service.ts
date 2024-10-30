import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RaffleCreationRequest } from '../../models/raffles/raffle-creation-request';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RafflesService {

  constructor(
    private httpClient: HttpClient
  ) { }

  private baseUrl: string = 'http://localhost:8082/api/v1/raffles';

  create(request: RaffleCreationRequest): Observable<string> {
    return this.httpClient.post(`${this.baseUrl}/create`, request, {responseType: 'text'}) as Observable<string>;
  }
}
