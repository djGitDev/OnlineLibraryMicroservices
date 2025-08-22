import {Role} from '../../models/Role';


export interface AuthState {
  accessToken: string | null;
  email: string | null;
  userId: number | null;
  role: Role | null
  cartId: number | null;


}

export const initialState: AuthState = {
  accessToken: null,
  email: null,
  userId: null,
  role: null,
  cartId: null


};
