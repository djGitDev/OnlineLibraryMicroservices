import {Component, Input, Output, EventEmitter, ChangeDetectorRef} from '@angular/core';
import { CartItem } from '../../models/cart/item';
import {MatDivider, MatList } from '@angular/material/list';
import {MatCard, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {trigger, transition, style, animate, state, keyframes} from '@angular/animations';
import {MatButton, MatFabButton, MatIconButton, MatMiniFabButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';
import {Book} from '../../models/inventary/book';


@Component({
  selector: 'app-cart',
  imports: [
    MatList,
    MatCard,
    MatCardTitle,
    MatDivider,
    MatIcon,
    MatMiniFabButton,
    MatButton,
    MatIconButton,
    MatFabButton,
    MatTooltip,

  ],
  templateUrl: './cart.html',
  animations: [
    trigger('slideIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-10px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ]),
    trigger('itemFade', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateX(-10px)' }),
        animate('200ms ease-out', style({ opacity: 1, transform: 'translateX(0)' }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0, transform: 'translateX(10px)' }))
      ])
    ]),
    trigger('bounce', [
      state('active', style({ transform: 'scale(1)' })),
      state('inactive', style({ transform: 'scale(1)' })),
      transition('* => active', [
        animate('600ms ease',
          keyframes([
            style({ transform: 'translateY(0)', offset: 0 }),
            style({ transform: 'translateY(-10px)', offset: 0.4 }),
            style({ transform: 'translateY(-5px)', offset: 0.6 }),
            style({ transform: 'translateY(0)', offset: 1 })
          ])
        )
      ])
    ])
  ]
})
export class CartComponent {

  @Input() books: Book[] = [];
  @Input() cartItems: CartItem[] = [];
  @Output() changeQuantityItemEvent = new EventEmitter<{ bookId: number, quantity: number }>();
  @Output() paymentEvent = new EventEmitter<void>();
  @Output() cartUpdated = new EventEmitter<Map<number, CartItem>>();


  constructor(private cdr: ChangeDetectorRef) {}

  isOpen = false;

  toggleCart() {
    this.isOpen = !this.isOpen;
    this.cdr.detectChanges();

  }

  onCartUpdated() {
    const cartMap = new Map<number, CartItem>();
    this.cartItems.forEach(item => {
      cartMap.set(item.bookId, item);
    });
    this.cartUpdated.emit(cartMap);
  }

  get total(): string {
    const total = this.cartItems.reduce((total, item) => total + item.quantity * item.price, 0);
    return Number.isInteger(total) ? total.toString() : total.toFixed(2);
  }

  getBookQuantity(bookId: number): number {
    return this.books.find(b => b.id === bookId)?.quantity || 0;
  }

  addItem(id: number, quantity: number) {
    this.changeQuantityItemEvent.emit({ bookId: id, quantity: -quantity });
  }
  removeItem(id: number, quantity: number) {
    this.changeQuantityItemEvent.emit({ bookId: id, quantity: quantity });
  }


  payment() {
    this.paymentEvent.emit();
    this.onCartUpdated();
  }
}

























