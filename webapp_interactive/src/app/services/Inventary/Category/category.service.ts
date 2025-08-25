import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Category } from '../../../models/inventary/category';
import {AlertService} from '../../Alert/alert.service';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private apiUrlInventary = environment.apiUrlInventary;

  constructor(private http: HttpClient, private alertService: AlertService) {}

  getCategories(): Observable<Category[]> {
    return this.http.get<{ categories: Category[] }>(`${this.apiUrlInventary}/categories`)
      .pipe(map(res => res.categories));
  }

  addCategory(param: { name: string }): Observable<Category> {
    return this.http.post<{ status: string, message: string, category: Category }>(
      `${this.apiUrlInventary}/categories/add`,
      param
    ).pipe(
      tap(res => {
        console.log('✅ Category added:', res);
        this.alertService.success('✅ Category added successfully');
      }),
      map(res => res.category)
    );
  }
}
