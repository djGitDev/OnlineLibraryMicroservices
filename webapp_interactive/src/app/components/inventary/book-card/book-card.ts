import {Component, EventEmitter, Input, Output, SimpleChanges} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Book } from '../../../models/inventary/book';
import {CartItem} from '../../../models/cart/item';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './book-card.html',
  styleUrls: ['./book-card.css']
})
export class BookCardComponent {
  @Input() book!: Book;
  @Output() addToCart = new EventEmitter<{ bookId: number; quantity: number; price:number }>();
  @Input() updatedItem?: CartItem;

  selectedQuantity: number = 1;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['updatedItem'] && this.updatedItem && this.book) {
      if (this.updatedItem.quantity >= this.book.quantity) {
        this.book.quantity = 0;
      } else {
        this.book.quantity -= this.updatedItem.quantity;
      }
      if (this.selectedQuantity > this.book.quantity) {
        this.selectedQuantity = this.book.quantity;
      }
    }
  }

  onAddToCart() {
    if (this.selectedQuantity > 0 && this.selectedQuantity <= this.book.quantity) {
      this.addToCart.emit({ bookId: this.book.id, quantity: this.selectedQuantity, price: this.book.price});
    }
  }
}

