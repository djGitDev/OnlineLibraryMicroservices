import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { logout } from '../../store/auth/auth.actions';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.html',
  imports: [
    MatIcon,
    MatButton
  ],
  styleUrls: ['./logout.css']
})
export class LogoutComponent {
  constructor(
    private store: Store,
    private router: Router
  ) {}

  onLogout() {
    this.store.dispatch(logout());
    this.router.navigate(['/login']);
  }
}
