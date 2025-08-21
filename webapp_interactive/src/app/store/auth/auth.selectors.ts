import { createFeatureSelector, createSelector } from '@ngrx/store';
import {AuthState} from './auth.state';


export const selectAuth = createFeatureSelector<AuthState>('auth');

export const selectIsAuthenticated = createSelector(
  selectAuth,
  state => !!state.accessToken
);

export const selectUserId = createSelector(
  selectAuth,
  state => state.userId
);

export const selectUserEmail = createSelector(
  selectAuth,
  state => state.email
);

export const selectUserRole = createSelector(
  selectAuth,
  state => state.role
);

export const selectCartId = createSelector(
  selectAuth,
  (auth: AuthState) => auth.cartId
);
