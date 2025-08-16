import { RegisterModel } from './register/register.model';
import {Credentials} from './login/credentials.model';

export class JsonConverter {
  static RegisterToJsonObject(model: RegisterModel): any {
    return {
      user: {
        first_name: model.user.first_name,
        last_name: model.user.last_name,
        email: model.user.email,
        phone: model.user.phone,
        password: model.user.password
      },
      address: {
        street: model.address.street,
        city: model.address.city,
        postal_code: model.address.postal_code,
        province: model.address.province,
        country: model.address.country
      }
    };
  }

  static CredentialsToJson(model: Credentials): any   {
    return {
      credentials: {
        email: model.email,
        password: model.password
      }
    };
  }




}
