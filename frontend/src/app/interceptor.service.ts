import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { DatabaseService } from './database.service';
import { switchMap, catchError } from 'rxjs/operators';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root',
})
export class InterceptorService implements HttpInterceptor {
  constructor(private db: DatabaseService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (this.db.isLoggedIn()) {
      let token = this.db.getAuthToken();
      let expirationDate = this.db.getExpirationDate();

      if (expirationDate <= Date.now()) {

        console.log('Token has expired');
        return this.db.refreshToken().pipe(
          switchMap((res) => {
            return next.handle(this.setHeader(req, res.refreshToken))
          })
        );
      }

      return next.handle(this.setHeader(req, token));
    }

    return next.handle(req);
  }

  setHeader(req: HttpRequest<any>, token: string) {
    return req.clone({
      headers: req.headers.append('Authorization', token),
    });
  }
}
