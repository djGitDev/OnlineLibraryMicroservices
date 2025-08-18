

export interface AuthState {
  accessToken: string | null;
  email: string | null
  role: 'ROLE_USER' | 'ROLE_ADMIN' | 'ROLE_FB-USER' | null;

}

export const initialState: AuthState = {
  accessToken: null,
  email: null,
  role: null

};
