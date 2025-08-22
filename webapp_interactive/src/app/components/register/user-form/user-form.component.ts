import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';


import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { User } from '../../../models/register/user.model';
import {MatCheckbox} from '@angular/material/checkbox';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatOptionModule, MatCheckbox],
  templateUrl: './user-form.component.html'
})
export class UserFormComponent {
  @Input() user!: User;

}
