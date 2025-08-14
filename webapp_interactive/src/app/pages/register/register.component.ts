// import { Component } from '@angular/core';
// import { FormsModule } from '@angular/forms';
// import { Router } from '@angular/router';
//
//
// // Angular Material imports
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatButtonModule } from '@angular/material/button';
// import { MatDividerModule } from '@angular/material/divider';
// import { MatOptionModule } from '@angular/material/core';
//
// @Component({
//   selector: 'app-register',
//   standalone: true,
//   templateUrl: './register.component.html',
//   styleUrls: ['./register.component.css'],
//   imports: [
//     FormsModule,
//     MatFormFieldModule,
//     MatInputModule,
//     MatSelectModule,
//     MatButtonModule,
//     MatDividerModule,
//     MatOptionModule
//   ]
// })
// export class RegisterComponent {
//   model = {
//     user: {
//       first_name: '',
//       last_name: '',
//       email: '',
//       phone: '',
//       password: ''
//     },
//     address: {
//       street: '',
//       city: '',
//       postal_code: '',
//       province: '',
//       country: 'Canada'
//     }
//   };
//   constructor(private router: Router) {}
//
//   onRegister() {
//     if (this.isFormValid()) {
//       console.log('Registration data:', this.model);
//       // Ici, tu peux envoyer les données à ton API
//     } else {
//       console.warn('Form is invalid. Please fill all required fields.');
//     }
//   }
//
//   private isFormValid(): boolean {
//     const u = this.model.user;
//     const a = this.model.address;
//     return !!(
//       u.first_name &&
//       u.last_name &&
//       u.email &&
//       u.phone &&
//       u.password &&
//       a.street &&
//       a.city &&
//       a.postal_code &&
//       a.province &&
//       a.country
//     );
//   }
//
//   goBackToLogin() {
//     this.router.navigate(['/login']);
//   }
// }
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterModel } from '../../models/register/register.model';
import { UserFormComponent } from '../../components/register/user-form/user-form.component';
import { AddressFormComponent } from '../../components/register/address-form/address-form.component';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [FormsModule, UserFormComponent, AddressFormComponent, MatButtonModule, MatDividerModule]
})
export class RegisterComponent {
  model = new RegisterModel();

  constructor(private router: Router) {}

  onRegister() {
    console.log('Registration data:', this.model);
  }

  goBackToLogin() {
    this.router.navigate(['/login']);
  }
}
