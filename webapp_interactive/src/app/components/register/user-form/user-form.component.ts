// import { Component, Input } from '@angular/core';
// import { FormsModule } from '@angular/forms';
//
//
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatButtonModule } from '@angular/material/button';
// import { MatOptionModule } from '@angular/material/core';
// import { User } from '../../../models/register/user.model';
// import {MatCheckbox} from '@angular/material/checkbox';
//
// @Component({
//   selector: 'app-user-form',
//   standalone: true,
//   imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatOptionModule, MatCheckbox],
//   templateUrl: './user-form.component.html'
// })
// export class UserFormComponent {
//   @Input() user!: User;
//
// }

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { User } from '../../../models/register/user.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import {NgStyle} from '@angular/common';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatButtonModule,
    NgStyle
  ],
  templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {
  @Input() user!: User;
  @Output() userChange = new EventEmitter<User>();
  @Output() userReset = new EventEmitter<void>();

  isConfirmed = false;
  userForm!: FormGroup;

  firstNameControl!: FormControl;
  lastNameControl!: FormControl;
  emailControl!: FormControl;
  phoneControl!: FormControl;
  passwordControl!: FormControl;
  roleControl!: FormControl;

  ngOnInit() {
    this.userForm = new FormGroup({
      first_name: new FormControl(this.user.first_name, Validators.required),
      last_name: new FormControl(this.user.last_name, Validators.required),
      email: new FormControl(this.user.email, [Validators.required, Validators.email]),
      phone: new FormControl(this.user.phone, Validators.required),
      password: new FormControl(this.user.password, Validators.required),
      role: new FormControl(this.user.role, Validators.required),
    });

    this.firstNameControl = this.userForm.get('first_name') as FormControl;
    this.lastNameControl = this.userForm.get('last_name') as FormControl;
    this.emailControl = this.userForm.get('email') as FormControl;
    this.phoneControl = this.userForm.get('phone') as FormControl;
    this.passwordControl = this.userForm.get('password') as FormControl;
    this.roleControl = this.userForm.get('role') as FormControl;
  }

  isFormValid() {
    return Object.values(this.userForm.controls).every(control => control.valid);
  }

  onConfirm() {
    if (!this.isConfirmed && this.isFormValid()) {
      this.isConfirmed = true;
      this.userChange.emit(this.userForm.value);
      Object.keys(this.userForm.controls).forEach(key => {
        this.userForm.get(key)?.disable({ emitEvent: false });
      });
    } else if (this.isConfirmed) {
      this.isConfirmed = false;
      this.userForm.reset();
      this.userForm.enable();
      this.userReset.emit();
    }
  }
}
