import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectUserEmail } from '../../store/auth/auth.selectors';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard-admin.html',
  imports: [
    AsyncPipe
  ],
  styleUrls: ['./dashboard-admin.css']
})
export class DashboardAdminComponent {
  userEmail: Observable<string | null>;

  constructor(private store: Store) {
    this.userEmail = this.store.select(selectUserEmail);
  }
}
