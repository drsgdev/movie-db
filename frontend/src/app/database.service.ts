import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LocalStorageService } from 'ngx-webstorage';
import { map, tap } from 'rxjs/operators';
import { Auth } from './auth';
import { Review } from './review';
import { Rating } from './rating';
import { RatingComponent } from './rating/rating.component';

@Injectable({
  providedIn: 'root',
})
export class DatabaseService {
  constructor(private http: HttpClient, private storage: LocalStorageService) {}

  isFavorite(id: number) {
    let payload = {
      id: id,
      username: this.getUsername(),
    };

    return this.http.post(environment.apiUrl + '/user/has_favorite', payload);
  }

  addVisited(id: number) {
    let payload = {
      id: id,
      username: this.getUsername(),
    };
    this.http
      .post(environment.apiUrl + '/user/add_visited', payload)
      .subscribe();
  }

  fetchVisited() {
    return this.http.get<any[]>(environment.apiUrl + '/user/visited', {
      params: {
        username: this.getUsername(),
      },
    });
  }

  fetchAllByType(type: string) {
    return this.http.get(environment.apiUrl + '/' + type + '/all');
  }

  fetchById(id: number) {
    return this.http.get(environment.apiUrl + '/find', {
      params: {
        id: id.toString(),
      },
    });
  }

  fetchCreditsById(id: number, type: string, creditType: string) {
    return this.http.get(
      environment.apiUrl + '/' + type + '/credits/' + creditType,
      {
        params: {
          id: id.toString(),
        },
      }
    );
  }

  fetchPersonById(id: number) {
    return this.http.get(environment.apiUrl + '/person/find', {
      params: {
        id: id.toString(),
      },
    });
  }

  fetchUser(username: string) {
    return this.http.get(environment.apiUrl + '/user/' + username);
  }

  signup(payload: any) {
    return this.http.post(environment.apiUrl + '/api/auth/signup', payload, {
      responseType: 'text',
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
      token: this.storage.retrieve('refreshToken'),
      username: this.storage.retrieve('username'),
    };

    this.storage.clear('authToken');
    this.storage.clear('refreshToken');
    this.storage.clear('expiresAt');

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
        responseType: 'text',
      })
      .subscribe(null, null, () => {
        this.storage.clear('authToken');
        this.storage.clear('refreshToken');
        this.storage.clear('expiresAt');
      });
  }

  rate(id: number, rate: number) {
    let body = new Rating();
    body.id = id;
    body.rate = rate;
    body.username = this.storage.retrieve('username');

    return this.http.post(environment.apiUrl + '/rate', body, {
      responseType: 'text',
    });
  }

  review(payload: Review) {
    payload.username = this.storage.retrieve('username');

    return this.http.post(environment.apiUrl + '/review', payload, {
      responseType: 'text',
    });
  }

  favorite(id: number) {
    let payload = {
      id: id,
      username: this.getUsername(),
    };
    return this.http.post(environment.apiUrl + '/user/add_favorite', payload, {
      responseType: 'text',
    });
  }

  removeFromFavorite(id: number) {
    let payload = {
      id: id,
      username: this.getUsername(),
    };

    return this.http.patch(
      environment.apiUrl + '/user/favorite/remove',
      payload,
      {
        responseType: 'text',
      }
    );
  }

  getRating(id: number) {
    return this.http.get<number[]>(environment.apiUrl + '/rate/get', {
      params: {
        id: id.toString(),
      },
    });
  }

  getReviews(id: number) {
    return this.http.get<Review[]>(environment.apiUrl + '/review/get', {
      params: {
        id: id.toString(),
      },
    });
  }

  getFavorites(username: string) {
    return this.http.get<any[]>(environment.apiUrl + '/user/favorites', {
      params: {
        username: username,
      },
    });
  }

  getRated(username: string) {
    return this.http.get<any[]>(environment.apiUrl + '/user/rated', {
      params: {
        username: username,
      },
    });
  }

  getReviewed(username: string) {
    return this.http.get<any[]>(environment.apiUrl + '/user/reviewed', {
      params: {
        username: username,
      },
    });
  }

  getAuthToken() {
    return this.storage.retrieve('authToken');
  }

  getExpirationDate() {
    return Number.parseInt(this.storage.retrieve('expiresAt'));
  }

  getUsername() {
    return this.storage.retrieve('username');
  }

  isLoggedIn() {
    if (this.storage.retrieve('authToken') != null) {
      return true;
    }

    return false;
  }
}
