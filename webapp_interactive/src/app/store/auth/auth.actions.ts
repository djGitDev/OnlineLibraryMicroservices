import { createAction, props } from '@ngrx/store';

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ accessToken: string; email: string; role: 'ROLE_USER' | 'ROLE_ADMIN' | 'ROLE_FB-USER'  }>()
);

export const logout = createAction('[Auth] Logout');
