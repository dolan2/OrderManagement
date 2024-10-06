import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Order} from '../model/order';
import {environment} from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }

  createOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, order);
  }

  updateOrder(order: Order): Observable<Order> {
    return this.http.patch<Order>(`${this.apiUrl}/${order.id}`, order);
  }
}
