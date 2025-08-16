
import {Component, model} from '@angular/core';
import { Router } from '@angular/router';
import { RegisterModel } from '../../models/register/register.model';
import { UserFormComponent } from '../../components/register/user-form/user-form.component';
import { AddressFormComponent } from '../../components/register/address-form/address-form.component';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import {AuthService} from '../../services/auth';
import {User} from '../../models/register/user.model';
import {Address} from '../../models/register/address.model';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [FormsModule, UserFormComponent, AddressFormComponent, MatButtonModule, MatDividerModule]
})
export class RegisterComponent {
  user: User = new User();
  address: Address = new Address();

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private router: Router, private authService:AuthService) {}

  onRegister() {
    const register = new RegisterModel(this.user, this.address);

    this.authService.register(register).subscribe({
      next: (response) => {
        this.successMessage = 'Inscription réussie !';
        console.log('Réponse serveur:', response);
        this.router.navigate(['/login']); // redirige vers login après succès
      },
      error: (err) => {
        // Gestion des erreurs HTTP
        console.error('Erreur inscription:', err);
        if (err.status === 400) {
          this.errorMessage = 'Données invalides. Vérifie ton formulaire.';
        } else if (err.status === 404) {
          this.errorMessage = 'Serveur introuvable. Vérifie ton API.';
        } else {
          this.errorMessage = 'Erreur serveur. Réessaie plus tard.';
        }
      }
    });
  }

  goBackToLogin() {
    this.router.navigate(['/login']);
  }

  protected readonly model = model;
}
