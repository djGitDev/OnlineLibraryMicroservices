// import { Component, Input } from '@angular/core';
// import { FormsModule } from '@angular/forms';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatOptionModule } from '@angular/material/core';
// import { Address } from '../../../models/register/address.model';
//
// @Component({
//   selector: 'app-address-form',
//   standalone: true,
//   imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatOptionModule],
//   templateUrl: './address-form.component.html'
// })
// export class AddressFormComponent {
//   @Input() address!: Address;
// }


import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Address } from '../../../models/register/address.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import {NgStyle} from '@angular/common';

@Component({
  selector: 'app-address-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatButtonModule,
    NgStyle,

  ],
  templateUrl: './address-form.component.html'
})
export class AddressFormComponent implements OnInit {
  @Input() address!: Address;
  @Output() addressChange = new EventEmitter<Address>();
  @Output() addressReset = new EventEmitter<void>();

  isConfirmed = false;
  addressForm!: FormGroup;

  streetControl!: FormControl;
  cityControl!: FormControl;
  provinceControl!: FormControl;
  postalCodeControl!: FormControl;
  countryControl!: FormControl;

  ngOnInit() {
    this.addressForm = new FormGroup({
      street: new FormControl(this.address.street,Validators.required),
      city: new FormControl(this.address.city, Validators.required),
      province: new FormControl(this.address.province, Validators.required),
      postal_code: new FormControl(this.address.postal_code, Validators.required),
      country: new FormControl(this.address.country, Validators.required)
    });



    this.streetControl = this.addressForm.get('street') as FormControl;
    this.cityControl = this.addressForm.get('city') as FormControl;
    this.provinceControl = this.addressForm.get('province') as FormControl;
    this.postalCodeControl = this.addressForm.get('postal_code') as FormControl;
    this.countryControl = this.addressForm.get('country') as FormControl;


  }

  isFormValid(){
    return Object.values(this.addressForm.controls).every(control => control.valid);
  }


  onConfirm() {
    if (!this.isConfirmed && this.isFormValid()) {
      this.isConfirmed = true;
      this.addressChange.emit(this.addressForm.value);
      Object.keys(this.addressForm.controls).forEach(key => {
        this.addressForm.get(key)?.disable({ emitEvent: false });
      });
    } else if (this.isConfirmed) {
      this.isConfirmed = false;
      this.addressForm.reset();
      this.addressForm.enable();
      this.addressReset.emit();
    }
  }

}
