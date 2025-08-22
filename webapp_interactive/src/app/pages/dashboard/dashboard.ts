import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Book } from '../../models/inventary/book';
import { CartService } from '../../services/cart.service';
import { BookCardComponent } from '../../components/inventary/book-card/book-card';
import { CartItem } from '../../models/cart/item';
import {BookService} from '../../services/book.service';
import {CartComponent} from '../../components/cart/cart';
import {Store} from '@ngrx/store';
import {selectUserRole} from '../../store/auth/auth.selectors';
import {Role} from '../../models/Role';
import {take} from 'rxjs';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {LogoutComponent} from '../../components/logout/logout';
import {MatDivider} from '@angular/material/divider';

@Component({
  selector: 'dashboard-user',
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


  constructor(private router:Router, private bookService: BookService, private cartService: CartService, private store:Store,private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.bookService.searchBooks().subscribe(books => {
      this.books = books;
    });

    this.cartService.cart$.subscribe(items => {
      this.cartItems = items;
    });

    this.cartService.getCartIfExist();
  }



  onCartUpdated(cartMap: Map<number, CartItem>) {
    this.cartItemsMap = cartMap;
  }

  // Called from BookCardComponent
  addToCart(bookId: number, quantity: number,price: number) {
    this.store.select(selectUserRole).pipe(take(1)).subscribe(role => {
      console.log('Current role:', role);
      if(role === Role.USER){
        this.cartService.addToCart(bookId, quantity,price);
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
  removeFromCart(bookId: number,quantity: number) {
    this.cartService.removeFromCart(bookId, quantity);
  }



  // Called from  CartComponent
  payment() {
    console.log('Checkout payload:', this.cartItems);
    this.cartService.payment();
  }
}
