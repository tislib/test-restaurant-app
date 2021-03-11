import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from '../service/authentication-service';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {API_AUTHENTICATION, PATH_REGISTER, PATH_TOKEN} from '../const/paths';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private auth: AuthenticationService, private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let ignoreAuthorization = false;

    if ((request.url === API_AUTHENTICATION + PATH_TOKEN) && request.method === 'POST') {
      ignoreAuthorization = true;
    }

    if (request.url === API_AUTHENTICATION + PATH_REGISTER) {
      ignoreAuthorization = true;
    }

    const accessToken = this.auth.getAccessToken();

    if (!ignoreAuthorization) {
      if (!accessToken) {
        this.router.navigate(['/login']);
        return throwError('request sent without token');
      }

      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    }

    return next.handle(request).pipe(tap(() => {

    }, err => {
      if (err.status === 401) {
        this.auth.validateToken();
      }
    }));
  }

}
