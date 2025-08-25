import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {BehaviorSubject} from 'rxjs';
import {CartItem} from '../../models/cart/item';
import {MatSnackBar} from '@angular/material/snack-bar';
import {selectCartId, selectUserId} from '../../store/auth/auth.selectors';
import {Store} from '@ngrx/store';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private cartItems: CartItem[] = [];
  private cartSubject = new BehaviorSubject<CartItem[]>([]);
  private apiUrlPayment = environment.apiUrlPayment;
  private userId: number | null = null;
  private cartId: number | null = null;
  itemsAfterPayment = this.cartSubject.asObservable();

  constructor(private http: HttpClient,private snackBar: MatSnackBar,private store: Store) {
    this.store.select(selectUserId).subscribe(id => {
      this.userId = id;
    });
    this.store.select(selectCartId).subscribe(id => {
      this.cartId = id;
    });
  }

  payment() {
    this.http.post(`${this.apiUrlPayment}/${this.cartId}/process/${this.userId}`, {})
      .subscribe({
        next: res => {
          console.log('✅ Payment processed:', res);
          this.cartItems.length = 0;
          this.cartSubject.next([...this.cartItems]);
          // ✅ Snackbar success message
          this.snackBar.open(
            '💳 Payment succeeded!\n' +
            '📧 An email will be sent to your address. \n' +
            '🚚 Another email will be sent after 10 seconds, simulating asynchronous delivery implemented with a Kafka event handler.',

          'Close',
            {
              duration: 20000,  // auto-close after 20s
              horizontalPosition: 'right',
              verticalPosition: 'top'
            }
          );
        },
        error: err => console.error('❌ Payment failed:', err)
      });
  }

}
