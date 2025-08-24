import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Author } from '../../../models/inventary/Author';

@Injectable({ providedIn: 'root' })
export class AuthorService {
  private apiUrlInventary = environment.apiUrlInventary;

  constructor(private http: HttpClient) {}

  getAuthors(): Observable<Author[]> {
    return this.http.get<{ authors: Author[] }>(`${this.apiUrlInventary}/authors`)
      .pipe(map(res => res.authors));
  }

  addAuthor(param: { name: string }): Observable<Author> {
    return this.http.post<{ status: string, message: string, author: Author }>(
      `${this.apiUrlInventary}/authors/add`,
      param
    ).pipe(
      tap(res => console.log('✅ Author ajouté:', res)),
      map(res => res.author)
    );
  }
}
