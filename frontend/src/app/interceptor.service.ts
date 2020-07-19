import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { DatabaseService } from './database.service';

@Injectable({
  providedIn: 'root',
})
export class InterceptorService implements HttpInterceptor {
  constructor(private db: DatabaseService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (this.db.hasToken()) {
      const token = this.db.getAuthToken();
      const expirationDate = this.db.getExpirationDate();

      if (expirationDate <= Date.now()) {
        return this.db
          .refreshToken()
          .pipe(
            switchMap((res) =>
              next.handle(this.setHeader(req, res.refreshToken))
            )
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
