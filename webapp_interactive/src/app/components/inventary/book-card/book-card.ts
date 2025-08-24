import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Book } from '../../../models/inventary/book';
import { CartItem } from '../../../models/cart/item';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIcon
  ],
  templateUrl: './book-card.html',
  styleUrls: ['./book-card.css']
})
export class BookCardComponent {
  @Input() book!: Book;

  // For User
  @Input() updatedItemAfterPayment?: CartItem;
  @Input() updatedItemBeforePayment?: CartItem;

  @Output() addToCart = new EventEmitter<CartItem>();

  // For Admin
  @Input() showDelete: boolean = false;
  @Output() deleteEvent = new EventEmitter<number>();

  selectedQuantity: number = 1;


  ngOnChanges(changes: SimpleChanges) {
    if (changes['updatedItemAfterPayment'] && this.updatedItemAfterPayment && this.book) {
      if (this.updatedItemAfterPayment.quantity >= this.book.quantity) {
        this.book.quantity = 0;
      } else {
        this.book.quantity -= this.updatedItemAfterPayment.quantity;
      }
      if (this.selectedQuantity > this.book.quantity) {
        this.selectedQuantity = this.book.quantity;
      }
    }
  }

  isAddDisabled(): boolean {
    return this.selectedQuantity > this.book.quantity
      || this.book.quantity === 0
      || (this.updatedItemBeforePayment !== undefined && this.updatedItemBeforePayment.quantity > 0);
  }

  onAddToCart() {
    if (this.selectedQuantity > 0 && this.selectedQuantity <= this.book.quantity) {
      this.addToCart.emit({ bookId: this.book.id, quantity: this.selectedQuantity, price: this.book.price, title: this.book.title });
    }
  }

  onDeleteClick() {
    this.deleteEvent.emit(this.book.id);
  }
}
