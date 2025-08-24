

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
// import {Category} from '../../../models/inventary/category';
// import {Publisher} from '../../../models/inventary/publisher';
// import {Author} from '../../../models/inventary/Author';
import {Book} from '../../../models/inventary/book';

@Injectable({ providedIn: 'root' })
export class BookService {

  private apiUrlInventary = environment.apiUrlInventary;

  constructor(private http: HttpClient) {}

  searchBooks(): Observable<Book[]> {
    return this.http.get<{status: string, message: string, books: Book[]}>(`${this.apiUrlInventary}/`)
      .pipe(
        tap(response => console.log('📚 Réponse complète API:', response)),
        map(response => response.books)
      );
  }

  addBook(book: Book): Observable<Book> {
    return this.http.post<{status: string, message: string, book: Book}>(`${this.apiUrlInventary}/add`, book)
      .pipe(
        tap(response => console.log('✅ Livre ajouté:', response)),
        map(response => response.book)
      );
  }

  deleteBook(bookId: number): Observable<void> {
    return this.http.delete<{status: string, message: string}>(`${this.apiUrlInventary}/${bookId}`)
      .pipe(
        tap(response => console.log('🗑️ Livre supprimé:', response)),
        map(() => void 0)
      );
  }


  // getAuthors(): Observable<Author[]> {
  //   return this.http.get<{ authors: Author[] }>(`${this.apiUrlInventary}/authors`)
  //     .pipe(map(res => res.authors));
  // }
  //
  // getPublishers(): Observable<Publisher[]> {
  //   return this.http.get<{ publishers: Publisher[] }>(`${this.apiUrlInventary}/publishers`)
  //     .pipe(map(res => res.publishers));
  // }
  //
  // getCategories(): Observable<Category[]> {
  //   return this.http.get<{ categories: Category[] }>(`${this.apiUrlInventary}/categories`)
  //     .pipe(
  //       map(res => res.categories)
  //     );
  // }
  //
  // addPublisher(param: { name: string }): Observable<Publisher> {
  //   return this.http.post<{ status: string, message: string, publisher: Publisher }>(
  //     `${this.apiUrlInventary}/publishers/add`,
  //     param
  //   ).pipe(
  //     tap(res => console.log('✅ Publisher ajouté:', res)),
  //     map(res => res.publisher)
  //   );
  // }
  //
  // addAuthor(param: { name: string }): Observable<Author> {
  //   return this.http.post<{ status: string, message: string, author: Author }>(
  //     `${this.apiUrlInventary}/authors/add`,
  //     param
  //   ).pipe(
  //     tap(res => console.log('✅ Author added:', res)),
  //     map(res => res.author)
  //   );
  // }
  //
  // addCategory(param: { name: string }): Observable<Category> {
  //   return this.http.post<{ status: string, message: string, category: Category }>(
  //     `${this.apiUrlInventary}/categories/add`,
  //     param
  //   ).pipe(
  //     tap(res => console.log('✅ Category added:', res)),
  //     map(res => res.category)
  //   );
  // }
}







