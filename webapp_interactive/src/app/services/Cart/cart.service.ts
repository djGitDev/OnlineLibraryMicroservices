import { Injectable } from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, switchMap, take} from 'rxjs';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {selectCartId, selectUserId} from '../../store/auth/auth.selectors';
import {Store} from '@ngrx/store';
import {setCartId} from '../../store/auth/auth.actions';
import {CartItem} from '../../models/cart/item';




@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartItems: CartItem[] = [];
  private cartSubject = new BehaviorSubject<CartItem[]>([]);
  private apiUrlCart = environment.apiUrlCart;

  private userId: number | null = null;
  private cartId: number | null = null;

  itemsAfterHandlingCart = this.cartSubject.asObservable();

   constructor(private http: HttpClient, private store: Store) {
     this.store.select(selectUserId).subscribe(id => {
       this.userId = id;
     });

  }

  async addToCart(itemToAdd: CartItem) {
    if (this.userId === null) return;
    // Backend api call
    this.http.post<AddToCartResponse>(
      `${this.apiUrlCart}/${this.userId}/add-item?bookId=${itemToAdd.bookId}&quantity=${itemToAdd.quantity}&price=${itemToAdd.price}`,
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
        //Update frontend
        const itemAlreadyAdded = this.cartItems.find(item => item.bookId === itemToAdd.bookId);
        if (itemAlreadyAdded) {
          itemAlreadyAdded.quantity += itemToAdd.quantity;
        } else {
          this.cartItems.push(itemToAdd);
        }
        this.cartSubject.next([...this.cartItems]);
      },
      error: (err) => console.error("❌ Error persisting:", err)
    });
  }



  changeQuantityFromCart(bookId: number, quantity: number) {
    if (this.userId === null) return;

    this.http.delete(
      `${this.apiUrlCart}/${this.userId}/clear-item?bookId=${bookId}&quantity=${quantity}`
    ).subscribe({
      next: res => {

        this.cartItems = this.cartItems.map(item => {
          if (item.bookId === bookId) {
            const newQuantity = item.quantity - quantity;
            return { ...item, quantity: newQuantity };
          }
          return item;
        }).filter(item => item.quantity > 0);

        // update frontend
        this.cartSubject.next([...this.cartItems]);

        if (this.cartItems.length === 0) {
          console.log("🗑️ Empty cart , removed...");
          this.http.delete(`${this.apiUrlCart}/${this.cartId}/clear-cart`).subscribe({
            next: () => {
              console.log("✅ Cart supprimé côté backend");
              this.cartId = null;
              this.store.dispatch(setCartId({ cartId: null }));

            },
            error: err => console.error("❌ Error while deleting cart:", err)
          });
        }

      },
      error: err => console.error("❌ Error removing item:", err)
    });
  }


  getCartIfExist(): Observable<CartItem[]> {
    return this.http.get<{ cartId: number }>(`${this.apiUrlCart}/${this.userId}`).pipe(
      switchMap(response => {
        if (response && response.cartId) {
          this.cartId = response.cartId;

          return this.http.get<any>(`${this.apiUrlCart}/items/${this.cartId}`).pipe(
            map(response => {
              const items: CartItem[] = response.items.map((item: any) => ({
                bookId: item.bookId,
                quantity: item.quantity,
                price: item.bookPrice,
                title: ''
              }));

              this.cartItems = items;
              this.cartSubject.next([...this.cartItems]);

              console.log('Formatted cartItems:', this.cartItems);
              return items;
            })
          );
        } else {
          console.log('No cart exists for this user');
          this.cartItems = [];
          this.cartSubject.next([...this.cartItems]);
          return of([]);
        }
      }),
      catchError(err => {
        console.error('Error fetching cart:', err);
        this.cartItems = [];
        this.cartSubject.next([...this.cartItems]);
        return of([]);
      })
    );
  }


  clearItems() {
    this.cartItems.length = 0;
    this.cartSubject.next([...this.cartItems]);
  }
}


interface AddToCartResponse {
  cartId: number;
}

