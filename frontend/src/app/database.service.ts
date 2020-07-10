import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LocalStorageService } from 'ngx-webstorage';
import { map, tap } from 'rxjs/operators';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root',
})
export class DatabaseService {
  constructor(private http: HttpClient, private storage: LocalStorageService) {}

  fetchAllByType(type: string) {
    return this.http.get(environment.apiUrl + '/' + type + '/all');
  }

  fetchById(id: number) {
    return this.http.get(environment.apiUrl + '/find?id=' + id);
  }

  fetchCreditsById(id: number, type: string, creditType: string) {
    return this.http.get(
      environment.apiUrl + '/' + type + '/credits/' + creditType + '?id=' + id
    );
  }

  signup(payload: any) {
    console.log(JSON.stringify(payload));
    return this.http.post(environment.apiUrl + '/api/auth/signup', payload, {
        responseType: 'text'
    });
  }

  login(payload: any) {
    return this.http
      .post<Auth>(environment.apiUrl + '/api/auth/login', payload)
      .pipe(
        map((data) => {
          this.storage.store('authToken', data.token);
          this.storage.store('username', data.username);
          this.storage.store('refreshToken', data.refreshToken);
          this.storage.store('expiresAt', data.expiresAt);

          return true;
        })
      );
  }

  refreshToken() {
    let payload = {
      refreshToken: this.storage.retrieve('refreshToken'),
      username: this.storage.retrieve('username'),
    };

    return this.http
      .post<Auth>(environment.apiUrl + '/api/auth/refresh', payload)
      .pipe(
        tap((res) => {
          this.storage.store('authToken', res.token);
          this.storage.store('refreshToken', res.refreshToken);
          this.storage.store('expiresAt', res.expiresAt);
        })
      );
  }

  logout() {
    let payload = {
      username: this.storage.retrieve('username'),
      token: this.storage.retrieve('refreshToken'),
    };

    this.http
      .post(environment.apiUrl + '/api/auth/logout', payload, {
          responseType: 'text'
      })
      .subscribe(null, null, () => {
        this.storage.clear('authToken');
        this.storage.clear('refreshToken');
        this.storage.clear('expiresAt');
      });
  }

  rate(id: number, rate: number) {
      return this.http.get(environment.apiUrl + '/rate?id=' + id + "&rate=" + rate);
  }

  getAuthToken() {
    return this.storage.retrieve('authToken');
  }

  isLoggedIn() {
    if (this.storage.retrieve('authToken') != null) {
      return true;
    }

    return false;
  }
}
