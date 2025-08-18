import { createReducer, on } from '@ngrx/store';
import { loginSuccess, logout } from './auth.actions';
import {initialState} from './auth.state';



export const authReducer = createReducer(
  initialState,
  on(loginSuccess, (state, { accessToken, email, role }) => ({ ...state, accessToken, email, role })),
  on(logout, state => ({ ...state, accessToken: null, email: null })),
);
