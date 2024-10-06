import {Component, inject, OnInit} from '@angular/core';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {OrderService} from '../service/order.service';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, ReactiveFormsModule, MatSnackBarModule, MatSelectModule, MatInputModule],
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {
  private _dialogRef = inject(MatDialogRef<OrderFormComponent>)
  private _snackBar = inject(MatSnackBar);
  private fb = inject(FormBuilder)
  private orderService = inject(OrderService);
  orderForm!: FormGroup;

  ngOnInit(): void {
    this.orderForm = this.fb.group({
      customer: this.fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        phone: ['', [Validators.required, Validators.pattern('^[- +()0-9]+$')]]
      }),
      orderDate: [new Date()],
      status: ['CREATED'],
      totalPrice: [{value: '', disabled: true}, Validators.required],
      orderItems: this.fb.array([])
    });

    this.addOrderItem();
  }

  get orderItems(): FormArray {
    return this.orderForm.get('orderItems') as FormArray;
  }

  addOrderItem(): void {
    const orderItem = this.fb.group({
      productName: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]]
    });

    this.orderItems.push(orderItem);
    this.updateTotalPrice();
  }

  removeOrderItem(index: number): void {
    this.orderItems.removeAt(index);
    this.updateTotalPrice();
  }

  updateTotalPrice(): void {
    const total = this.orderItems.controls.reduce((acc, curr) => {
      const price = curr.get('price')?.value || 0;
      const quantity = curr.get('quantity')?.value || 0;
      return acc + (price * quantity);
    }, 0);

    this.orderForm.patchValue({ totalPrice: total.toFixed(2) });
  }

  onSubmit(): void {
    if (this.orderForm.valid) {
      this.orderForm.get('totalPrice')?.enable();
      this.orderService.createOrder(this.orderForm.value).subscribe({
        next: () => {
          this._snackBar.open('Order created successfully', 'Close', {duration: 3000})
        },
        error: () => {
          this._snackBar.open('Error creating order', 'Close', {duration: 3000})
        }
      });

      this.orderForm.get('totalPrice')?.disable();
      this._dialogRef.close(this.orderForm.value);
    }
  }

}
