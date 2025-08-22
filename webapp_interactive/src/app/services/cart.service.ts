import { Injectable } from '@angular/core';
import {BehaviorSubject,take} from 'rxjs';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {selectCartId, selectUserId} from '../store/auth/auth.selectors';
import {Store} from '@ngrx/store';
import {setCartId} from '../store/auth/auth.actions';

export interface CartItem {
  bookId: number;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {


  private cartItems: CartItem[] = [];
  private cartSubject = new BehaviorSubject<CartItem[]>([]);
  private apiUrlCart = environment.apiUrlCart;
  private apiUrlPayment = environment.apiUrlPayment;

  private userId: number | null = null;
  private cartId: number | null = null;

  cart$ = this.cartSubject.asObservable();

   constructor(private http: HttpClient, private store: Store) {
     this.store.select(selectUserId).subscribe(id => {
       this.userId = id;
     });
  }

  async addToCart(bookId: number, quantity: number = 1, price: number) {
    if (this.userId === null) return; // sécurité

    // Backend api call
    this.http.post<AddToCartResponse>(
      `${this.apiUrlCart}/${this.userId}/add-item?bookId=${bookId}&quantity=${quantity}&price=${price}`,
      {}
    ).subscribe({
      next: (res) => {
        console.log("✅ Persisted in backend:", res);

        if (res.cartId) {
          this.store.dispatch(setCartId({ cartId: res.cartId }));
        }
        this.store.select(selectCartId).pipe(take(1)).subscribe(id => {
          this.cartId = id;
        });

        // Update frontend
        const existing = this.cartItems.find(item => item.bookId === bookId);
        if (existing) {
          existing.quantity += quantity;
        } else {
          this.cartItems.push({ bookId, quantity });
        }
        this.cartSubject.next([...this.cartItems]);
      },
      error: (err) => console.error("❌ Error persisting:", err)
    });
  }



  removeFromCart(bookId: number, quantity: number) {
    if (this.userId === null) return;

    this.http.delete(
      `${this.apiUrlCart}/${this.userId}/clear-item?bookId=${bookId}&quantity=${quantity}`
    ).subscribe({
      next: res => {
        console.log("✅ Item removed from backend:", res);

        this.cartItems = this.cartItems.map(item => {
          if (item.bookId === bookId) {
            const newQuantity = item.quantity - quantity;
            return { ...item, quantity: newQuantity };
          }
          return item;
        }).filter(item => item.quantity > 0);

        // notifier le frontend
        this.cartSubject.next([...this.cartItems]);

        if (this.cartItems.length === 0) {
          console.log("🗑️ Panier vide, suppression du cart côté backend...");
          this.http.delete(`${this.apiUrlCart}/${this.cartId}/clear-cart`).subscribe({
            next: () => {
              console.log("✅ Cart supprimé côté backend");
              this.cartId = null;
              this.store.dispatch(setCartId({ cartId: null }));

            },
            error: err => console.error("❌ Erreur suppression du cart:", err)
          });
        }

      },
      error: err => console.error("❌ Error removing item:", err)
    });
  }


  payment() {
    this.http.post(`${this.apiUrlPayment}/${this.cartId}/process/${this.userId}`, {})
      .subscribe({
        next: res => {
          console.log('✅ Payment processed:', res);

          // Vider le panier local
          this.cartItems = [];
          this.cartSubject.next([...this.cartItems]);
        },
        error: err => console.error('❌ Payment failed:', err)
      });
  }

  getCartIfExist(): void {
    this.http.get<{ cartId: number }>(`${this.apiUrlCart}/${this.userId}`)
      .subscribe({
        next: (response) => {
          console.log('Raw API response:', response);

          if (response && response.cartId) {
            this.cartId = response.cartId;
            this.http.get<any>(`${this.apiUrlCart}/items/${this.cartId}`)
              .subscribe({
                next: (response) => {
                  console.log('Raw API items response:', response);
                  this.cartItems = response.items.map((item: any) => ({
                    bookId: item.bookId,
                    quantity: item.quantity
                  }));
                  console.log('Formatted cartItems:', this.cartItems);
                  this.cartSubject.next([...this.cartItems]);
                },
                error: (err) => console.error('Error fetching items:', err)
              });

          } else {
            console.log('No cart exists for this user');
            this.cartItems = [];
          }
        },
        error: (err) => console.error('Error fetching cartId:', err)
      });
  }
}


interface AddToCartResponse {
  cartId: number;
}

