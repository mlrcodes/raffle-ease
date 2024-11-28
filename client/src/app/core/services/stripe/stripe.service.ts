import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StripeService {

  constructor(
    private httpClient: HttpClient
  ) { }

  private baseURL: string = 'http://13.51.175.242:8080/api/v1/stripe';

  getPublicKey(): Observable<string> {
    return this.httpClient.get(`${this.baseURL}/public-key`, {responseType: 'text'}) as Observable<string>;
  } 

}