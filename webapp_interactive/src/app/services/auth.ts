import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';
import {JsonConverter} from '../models/json.converter.util';
import {RegisterModel} from '../models/register/register.model';
import {Credentials} from '../models/login/credentials.model';

@Injectable({
  providedIn: 'root'

})
export class AuthService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  register(data: RegisterModel): Observable<any> {
    const jsonData = JsonConverter.RegisterToJsonObject(data);
    console.log('JSON à envoyer:', jsonData);
    return this.http.post(`${this.apiUrl}/register`, jsonData,
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

    return this.http.post(`${this.apiUrl}/login`, jsonData,
      {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
  }
}
