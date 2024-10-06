import {Customer} from './customer';
import {OrderItem} from './orderItem';
import {OrderStatus} from './orderStatus';

export interface Order {
  id?: number;
  customer: Customer;
  orderDate: Date;
  updatedAt: Date;
  status: OrderStatus;
  totalPrice: number;
  orderItems: OrderItem[];
}
