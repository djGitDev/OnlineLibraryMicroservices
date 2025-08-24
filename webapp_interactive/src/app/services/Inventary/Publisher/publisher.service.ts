import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Publisher } from '../../../models/inventary/publisher';

@Injectable({ providedIn: 'root' })
export class PublisherService {
  private apiUrlInventary = environment.apiUrlInventary;

  constructor(private http: HttpClient) {}

  getPublishers(): Observable<Publisher[]> {
    return this.http.get<{ publishers: Publisher[] }>(`${this.apiUrlInventary}/publishers`)
      .pipe(map(res => res.publishers));
  }

  addPublisher(param: { name: string }): Observable<Publisher> {
    return this.http.post<{ status: string, message: string, publisher: Publisher }>(
      `${this.apiUrlInventary}/publishers/add`,
      param
    ).pipe(
      tap(res => console.log('✅ Publisher ajouté:', res)),
      map(res => res.publisher)
    );
  }
}
