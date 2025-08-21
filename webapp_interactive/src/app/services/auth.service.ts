import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import {firstValueFrom, Observable, tap} from 'rxjs';
import {environment} from '../../environments/environment';
import {JsonConverter} from '../models/json.converter.util';
import {RegisterModel} from '../models/register/register.model';
import {Credentials} from '../models/login/credentials.model';
import {Role} from '../models/Role';

@Injectable({
  providedIn: 'root'

})
export class AuthService {

  private apiUrlProfil = environment.apiUrlProfil;


  constructor(private http: HttpClient) {}

  register(data: RegisterModel): Observable<any> {
    const jsonData = JsonConverter.RegisterToJsonObject(data);
    console.log('JSON à envoyer:', jsonData);
    return this.http.post(`${this.apiUrlProfil}/register`, jsonData,
      {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
  }

  login(credentials: Credentials): Observable<any> {
    const jsonData = JsonConverter.CredentialsToJson(credentials);
    console.log('JSON à envoyer:', jsonData);

    return this.http.post(`${this.apiUrlProfil}/login`, jsonData,
      {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        withCredentials: true

      });
  }

  async loginFbAccount(): Promise<{
    user_id: number;
    facebook_access_token: string;
    app_jwt_token: string;
    user_email: string;
    role: Role | null;
    }> {
    const res = await firstValueFrom(
      this.http.get<{ auth_url: string }>(`${this.apiUrlProfil}/facebook/auth_url`)
    );

    return new Promise((resolve, reject) => {
      const popup = window.open(res.auth_url, 'FacebookAuth', 'width=600,height=700');
      if (!popup) {
        return reject(new Error('Impossible d’ouvrir la popup'));
      }

      const messageHandler = (event: MessageEvent) => {
        if (event.origin !== window.location.origin) return;

        const data: {
          facebook_access_token: string;
          app_jwt_token: string;
          user_email: string;
          user_id: number;
          role: string; // <- string reçu du backend
        } = event.data;

        window.removeEventListener('message', messageHandler);

        // Convert string -> enum
        let roleEnum: Role | null;
        switch (data.role) {
          case 'ROLE_ADMIN':
            roleEnum = Role.ADMIN;
            break;
          case 'ROLE_FB-USER':
            roleEnum = Role.FB_USER;
            break;
          case 'ROLE_USER':
            roleEnum = Role.USER;
            break;
          default:
            roleEnum = null;
            break;
        }

        resolve({
          facebook_access_token: data.facebook_access_token,
          app_jwt_token: data.app_jwt_token,
          user_id:data.user_id,
          user_email: data.user_email,
          role: roleEnum
        });
      };

      window.addEventListener('message', messageHandler, false);
    });
  }
}


