import {Component} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {LogoutComponent} from '../../../components/logout/logout';
import {MatDivider} from '@angular/material/divider';

@Component({
  selector: 'app-dashboard-user-main-inventory',
  imports: [
    MatIcon,
    MatButton,
    LogoutComponent,
    MatDivider,
  ],
  templateUrl: './dashboard-admin.html'
})
export class DashboardAdminComponent {
  constructor(private router: Router) {
  }

  goToAddBook() {
    this.router.navigate(['/admin-add']);
  }

  goToRemoveBook() {
    this.router.navigate(['/admin-remove']);
  }

}
