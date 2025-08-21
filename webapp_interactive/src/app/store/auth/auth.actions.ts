import { createAction, props } from '@ngrx/store';
import {Role} from '../../models/Role';

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ accessToken: string; email: string;userId: number | null; role: Role | null }>()
);

export const logout = createAction('[Auth] Logout');

export const setCartId = createAction(
  '[Auth] Set Cart ID',
  props<{ cartId: number | null }>()
);

