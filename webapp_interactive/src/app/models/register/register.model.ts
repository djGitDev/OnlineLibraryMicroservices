import { User } from './user.model';
import { Address } from './address.model';

export class RegisterModel {
  user: User;
  address: Address;

  constructor(user: User, address: Address) {
    this.user = user;
    this.address = address;
  }
}
