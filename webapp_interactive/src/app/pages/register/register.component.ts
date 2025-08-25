
import {Component, ElementRef, model, ViewChild} from '@angular/core';
import { Router } from '@angular/router';
import { RegisterModel } from '../../models/register/register.model';
import { UserFormComponent } from '../../components/register/user-form/user-form.component';
import { AddressFormComponent } from '../../components/register/address-form/address-form.component';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import {AuthService} from '../../services/Profil/auth.service';
import {User} from '../../models/register/user.model';
import {Address} from '../../models/register/address.model';
import {AlertService} from '../../services/Alert/alert.service';
import {NgStyle} from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [FormsModule, UserFormComponent, AddressFormComponent, MatButtonModule, MatDividerModule, AddressFormComponent, NgStyle]
})
export class RegisterComponent {
  user: User = new User();
  address: Address = new Address();
  isUserValid: boolean = false;
  isAddressValid: boolean = false;

  @ViewChild(AddressFormComponent) confirmBtnAddress!: AddressFormComponent;
  @ViewChild(UserFormComponent) confirmBtnUser!: UserFormComponent;


  constructor(private router: Router, private authService:AuthService,private alertService: AlertService) {}

  onUserReset() {
    this.user = new User();
    this.isUserValid = false;
  }

  onUserChange(user: User) {
    this.user = user;
    this.isUserValid = true;
  }
  onAddressChange(address: Address) {
    this.address = address;
    this.isAddressValid = true;
  }
  onAddressReset() {
    this.address = new Address();
    this.isAddressValid = false;
  }

  onRegister() {
    if (!this.isAddressValid || !this.isUserValid) {
      this.alertService.alert('Please complete the address correctly.');
      return;
    }
    const register = new RegisterModel(this.user, this.address);

    this.authService.register(register).subscribe({
      next: (response) => {
        console.log('Backend response after success register:', response);
        this.alertService.success('✅ Successfully registered.');
        this.isAddressValid = false;
        this.isUserValid = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.alertService.error(err);
        this.isAddressValid = false;
        this.isUserValid = false;
        this.confirmBtnAddress.onConfirm();
        this.confirmBtnUser.onConfirm()
      }
    });

  }

  goBackToLogin() {
    this.router.navigate(['/login']);
  }

  protected readonly model = model;


}
