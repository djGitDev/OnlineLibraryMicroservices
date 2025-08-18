import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';

// Angular Material imports
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDividerModule} from '@angular/material/divider';
import {AuthService} from '../../services/auth';
import {Credentials} from '../../models/login/credentials.model';
import {Store} from '@ngrx/store';
import {loginSuccess} from '../../store/auth/auth.actions';

@Component({
  selector: 'login-page',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDividerModule,
    RouterLink
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';

  constructor(private router: Router, private authService:AuthService, private store: Store
  ) {}


  onLogin() {
    const credentials = new Credentials(this.email, this.password);
    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Login success', response);
        const {accessToken, email,role} = response;
        this.store.dispatch(loginSuccess({ accessToken: accessToken , email, role }));
        this.store.subscribe(state => {
          console.log('🔥 State AFTER loginSuccess:', state);
        });
        if (role === 'ROLE_ADMIN') {
          this.router.navigate(['/dashboard-admin']);
        } else if (role === 'ROLE_USER') {
          this.router.navigate(['/dashboard-user']);
        } else {
          this.router.navigate(['/login']); // fallback
        }
      },
      error: (err) => {
        console.error('Erreur login', err);
      }
    });
  }

  loginWithFacebook() {
    console.log('Connexion via Facebook');
    // Ajouter logique OAuth
  }
}

