import {AfterViewInit, Component, inject, OnInit, ViewChild} from '@angular/core';
import {Order} from '../model/order';
import {OrderService} from '../service/order.service';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {CommonModule} from '@angular/common';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {OrderFormComponent} from '../order-form/order-form.component';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {OrderStatus} from '../model/orderStatus';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatDialogModule, MatButtonModule, MatSelectModule, MatSnackBarModule,
    MatSortModule, MatPaginatorModule, MatFormFieldModule, MatInputModule],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css'
})
export class OrderListComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['id', 'customer', 'orderDate', 'status', 'totalPrice'];
  dataSource: MatTableDataSource<Order> = new MatTableDataSource<Order>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  orderStatuses = Object.values(OrderStatus).filter(value => typeof value === 'string');

  private _snackBar = inject(MatSnackBar);
  private _matDialog = inject(MatDialog);
  private orderService = inject(OrderService);

  ngOnInit(): void {
    this.fetchOrders();

    this.dataSource.filterPredicate = (data: any, filter: string) => {
      const transformedFilter = filter.trim().toLowerCase();
      const dataStr = `${data.id} ${data.customer.firstName} ${data.customer.lastName} ${data.orderDate} ${data.status} ${data.totalPrice}`.toLowerCase();
      return dataStr.includes(transformedFilter);
    }
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  fetchOrders(): void {
    this.orderService.getAllOrders().subscribe(orders => {
      this.dataSource.data = orders;
    });
  }

  updateOrderStatus(order: Order) {
    this.orderService.updateOrder(order).subscribe({
      next: () => {
        this._snackBar.open('Order status updated successfully', 'Close', {duration: 3000})
      },
      error: () => {
        this._snackBar.open('Error updating order status', 'Close', {duration: 3000})
      }
    });
  }

  openOrderForm(): void {
    const dialogRef = this._matDialog.open(OrderFormComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.fetchOrders();
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
