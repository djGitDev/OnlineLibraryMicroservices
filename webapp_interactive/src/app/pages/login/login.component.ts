import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';

// Angular Material imports
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDividerModule} from '@angular/material/divider';
import {AuthService} from '../../services/auth';
import {RegisterModel} from '../../models/register/register.model';
import {Credentials} from '../../models/login/credentials.model';

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

  constructor(private router: Router, private authService:AuthService) {}


  onLogin() {
    const credentials = new Credentials(this.email, this.password);
    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Login réussi', response);
        // redirection après succès
        // this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Erreur login', err);
        // afficher un message d'erreur à l'utilisateur si nécessaire
      }
    });
  }

  loginWithFacebook() {
    console.log('Connexion via Facebook');
    // Ajouter logique OAuth
  }
}

