import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';

// Angular Material imports
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDividerModule} from '@angular/material/divider';
import {AuthService} from '../../services/Profil/auth.service';
import {Credentials} from '../../models/login/credentials.model';
import {Store} from '@ngrx/store';
import {loginSuccess} from '../../store/auth/auth.actions';
import {Role} from '../../models/Role';

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
        const {accessToken, email,userId,role} = response;
        this.store.dispatch(loginSuccess({ accessToken: accessToken , email, userId ,role }));
        this.store.subscribe(state => {
          console.log('🔥 State AFTER loginSuccess:', state);
        });
        if (role === Role.ADMIN) {
          this.router.navigate(['/dashboard-admin']);
        } else if (role ===   Role.USER) {
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

  async loginWithFacebook() {
    console.log('Connexion via Facebook');
    try {
      const fbData = await this.authService.loginFbAccount();
      console.log('Tokens récupérés:', fbData);

      this.store.dispatch(loginSuccess({
        accessToken: fbData.app_jwt_token,
        userId : null,
        email: fbData.user_email,
        role: fbData.role
      }));
      this.store.subscribe(state => {
        console.log('🔥 State AFTER loginSuccess:', state);
      });
      if (fbData.role === Role.FB_USER) {
        this.router.navigate(['/dashboard-fbuser']);
      } else {
        this.router.navigate(['/login']);
      }
    } catch (err) {
      console.error('Erreur login Facebook', err);
    }
  }
}

