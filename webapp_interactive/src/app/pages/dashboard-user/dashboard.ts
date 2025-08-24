import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Book } from '../../models/inventary/book';
import { CartService } from '../../services/Cart/cart.service';
import { BookCardComponent } from '../../components/inventary/book-card/book-card';
import { CartItem } from '../../models/cart/item';
import {BookService} from '../../services/Inventary/Book/book.service';
import {CartComponent} from '../../components/cart/cart';
import {Store} from '@ngrx/store';
import {selectUserRole} from '../../store/auth/auth.selectors';
import {Role} from '../../models/Role';
import {switchMap, take} from 'rxjs';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {LogoutComponent} from '../../components/logout/logout';
import {MatDivider} from '@angular/material/divider';
import {PaymentService} from '../../services/Payment/payment.service';

@Component({
  selector: 'dashboard-user-user',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  imports: [
    CommonModule,
    BookCardComponent,
    CartComponent,
    LogoutComponent,
    MatDivider
  ]
})
export class DashboardUserComponent implements OnInit {
  books: Book[] = [];
  cartItems: CartItem[] = [];
  cartItemsMap = new Map<number, CartItem>();


  constructor(private router:Router, private bookService: BookService, private cartService: CartService,private paymentService: PaymentService, private store:Store,private snackBar: MatSnackBar) {}

  ngOnInit() {
    // Books -> Cart
    this.bookService.searchBooks().pipe(
      switchMap(books => {
        this.books = books;
        return this.cartService.getCartIfExist();
      })
    ).subscribe(cart => {
      this.cartItems = this.mapItemsWithBooks(cart);
    });

    // Cart events
    this.cartService.itemsAfterHandlingCart.subscribe(items => {
      this.cartItems = this.mapItemsWithBooks(items);
    });

    // Payment events
    this.paymentService.itemsAfterPayment.subscribe(items => {
      this.cartItems = this.mapItemsWithBooks(items);
      this.cartService.clearItems();
    });
  }

  private mapItemsWithBooks(items: CartItem[]): CartItem[] {
    return items.map(item => {
      const book = this.books.find(b => b.id === item.bookId);
      if (!book) {
        return {
          ...item,
          title: 'Unknown',
        };
      }
      const adjustedQuantity = Math.min(item.quantity, book.quantity);
      return {
        ...item,
        title: book.title,
        quantity: adjustedQuantity
      };
    });
  }

  getUpdatedItemBeforePayment(bookId: number): CartItem | undefined {
    return this.cartItems.find(item => item.bookId === bookId);
  }

  onCartUpdated(cartMap: Map<number, CartItem>) {
    this.cartItemsMap = cartMap;
  }

  // Called from BookCardComponent
  addToCart(item: CartItem) {
    this.store.select(selectUserRole).pipe(take(1)).subscribe(role => {
      console.log('Current role:', role);
      if(role === Role.USER){
        this.cartService.addToCart(item);
      } else {
        this.snackBar.open(
          'Please create an account to add items to your cart',
          'Register',
          { duration: 5000 }
        ).onAction().subscribe(() => {
          this.router.navigate(['/register']);
        });
      }
    });
  }

  // Called from  CartComponent
  changeQuantityFromCart(bookId: number,quantity: number) {
    this.cartService.changeQuantityFromCart(bookId, quantity);
  }



  // Called from  CartComponent
  payment() {
    console.log('Checkout payload:', this.cartItems);
    this.paymentService.payment();
  }
}


