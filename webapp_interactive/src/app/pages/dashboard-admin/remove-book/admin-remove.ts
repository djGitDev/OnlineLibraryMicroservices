import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import {BookService} from '../../../services/Inventary/Book/book.service';
import {Book} from '../../../models/inventary/book';
import {BookCardComponent} from '../../../components/inventary/book-card/book-card';

@Component({
  selector: 'admin-remove',
  templateUrl: './admin-remove.html',
  imports: [
    BookCardComponent
  ],
  styleUrls: ['./admin-remove.css']
})
export class AdminRemoveComponent implements OnInit {

  books: Book[] = [];

  constructor(
    private bookService: BookService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.bookService.searchBooks().subscribe({
      next: (books) => this.books = books,
      error: (err) => {
        console.error('Error loading books:', err);
        this.snackBar.open('Failed to load books', 'Close', { duration: 3000 });
      }
    });
  }

  onDeleteBook(bookId: number): void {
    if (!confirm('Are you sure you want to delete this book?')) {
      return;
    }

    this.bookService.deleteBook(bookId).subscribe({
      next: () => {
        // Remove the deleted book from the local array
        this.books = this.books.filter(book => book.id !== bookId);
        this.snackBar.open('Book deleted successfully', 'Close', { duration: 3000 });
      },
      error: (err) => {
        console.error('Error deleting book:', err);
        this.snackBar.open('Failed to delete book', 'Close', { duration: 3000 });
      }
    });
  }
}
