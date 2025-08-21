import { createReducer, on } from '@ngrx/store';
import {loginSuccess, logout, setCartId} from './auth.actions';
import {initialState} from './auth.state';



export const authReducer = createReducer(
  initialState,
  on(loginSuccess, (state, { accessToken, email,userId , role }) => ({ ...state, accessToken, email,userId, role })),
  on(logout, state => ({ ...state, accessToken: null, email: null })),
  on(setCartId, (state, { cartId }) => ({ ...state, cartId })),
);
