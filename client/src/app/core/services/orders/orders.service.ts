import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OrderRequest } from '../../models/orders/order-request';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  constructor(
    private httpClient: HttpClient
  ) { }

  private baseURL: string = 'http://localhost:8080/api/v1/orders';

  purchase(orderRequest: OrderRequest): Observable<string> {
    return this.httpClient.post(`${this.baseURL}/create-order`, orderRequest, { responseType: 'text'} ) as Observable<string>;
  }   
}