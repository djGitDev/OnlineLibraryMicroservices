import { User } from './user.model';
import { Address } from './address.model';

export class RegisterModel {
  user: User = new User();
  address: Address = new Address();
}
