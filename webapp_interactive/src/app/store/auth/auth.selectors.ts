import { createFeatureSelector, createSelector } from '@ngrx/store';
import {AuthState} from './auth.state';


export const selectAuth = createFeatureSelector<AuthState>('auth');

export const selectIsAuthenticated = createSelector(
  selectAuth,
  state => !!state.accessToken
);

export const selectUserEmail = createSelector(
  selectAuth,
  state => state.email
);

export const selectUserRole = createSelector(
  selectAuth,
  state => state.role
);
