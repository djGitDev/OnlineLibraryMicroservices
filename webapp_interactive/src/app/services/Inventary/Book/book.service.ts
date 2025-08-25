

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {map, Observable, switchMap, tap} from 'rxjs';
import { environment } from '../../../../environments/environment';
import {Book} from '../../../models/inventary/book';

@Injectable({ providedIn: 'root' })
export class BookService {

  private apiUrlInventary = environment.apiUrlInventary;
  private apiUrlCart = environment.apiUrlCart;

  constructor(private http: HttpClient) {}

  searchBooks(): Observable<Book[]> {
    return this.http.get<{status: string, message: string, books: Book[]}>(`${this.apiUrlInventary}/`)
      .pipe(
        tap(response => console.log('📚 Api response from inventory:', response)),
        map(response => response.books)
      );
  }

  addBook(book: Book): Observable<Book> {
    return this.http.post<{status: string, message: string, book: Book}>(`${this.apiUrlInventary}/add`, book)
      .pipe(
        tap(response => console.log('✅ Book added:', response)),
        map(response => response.book)
      );
  }

  // deleteBook(bookId: number): Observable<void> {
  //   return this.http.delete<{status: string, message: string}>(`${this.apiUrlInventary}/${bookId}`)
  //     .pipe(
  //       tap(response => console.log('🗑️ Book deleted:', response)),
  //       map(() => void 0)
  //     );
  // }

  deleteBook(bookId: number): Observable<void> {
    return this.http.delete<{ status: string, message: string }>(`${this.apiUrlInventary}/${bookId}`)
      .pipe(
        tap(response => console.log(`🗑️ Book deleted (ID: ${bookId}):`, response)),
        switchMap(() => {
          return this.http.delete<void>(`${this.apiUrlCart}/clearItemsByBook/${bookId}`);
        }),
        tap(() => console.log(`🗑️ All cart items with Book ID ${bookId} deleted`))
      );
  }
}







