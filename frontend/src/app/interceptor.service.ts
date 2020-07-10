import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { DatabaseService } from './database.service';
import { switchMap } from 'rxjs/operators';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {

  isTokenRefreshing = false;
  behavior : BehaviorSubject<any> = new BehaviorSubject(null);

  constructor(private db: DatabaseService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = this.db.getAuthToken();
    if (token) {
      this.setHeader(req, token);
    }

    return next.handle(req);
  }

  setHeader(req: HttpRequest<any>, token: any) {
    return req.clone({
      headers: req.headers.set('Authorization', token)
    });
  }

  private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.behavior.next(null);

      return this.db.refreshToken().pipe(
        switchMap((res: Auth) => {
          this.isTokenRefreshing = false;
          this.behavior.next(res.token);

          return next.handle(this.setHeader(req, res.token));
        })
      )
    }
  }
}
