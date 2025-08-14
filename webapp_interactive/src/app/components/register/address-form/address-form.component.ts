import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { Address } from '../../../models/register/address.model';

@Component({
  selector: 'app-address-form',
  standalone: true,
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatOptionModule],
  templateUrl: './address-form.component.html'
})
export class AddressFormComponent {
  @Input() address!: Address;
}
