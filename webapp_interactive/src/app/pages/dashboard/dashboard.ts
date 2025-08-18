import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectUserEmail } from '../../store/auth/auth.selectors';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  imports: [
    AsyncPipe
  ],
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent {
  userEmail: Observable<string | null>;

  constructor(private store: Store) {
    this.userEmail = this.store.select(selectUserEmail);
  }
}
