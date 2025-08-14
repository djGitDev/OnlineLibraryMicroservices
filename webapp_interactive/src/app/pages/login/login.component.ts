import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';

// Angular Material imports
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDividerModule} from '@angular/material/divider';

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

  onLogin() {
    console.log('Email:', this.email, 'Password:', this.password);
    // Ajouter logique d'authentification
  }

  loginWithFacebook() {
    console.log('Connexion via Facebook');
    // Ajouter logique OAuth
  }
}

