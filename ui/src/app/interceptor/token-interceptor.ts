import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from '../service/authentication-service';
import {Router} from '@angular/router';
import {API_AUTHENTICATION, PATH_REGISTER, PATH_TOKEN} from '../const/paths';
import {catchError} from 'rxjs/operators';
import {flatMap} from 'rxjs/internal/operators';


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

    if (ignoreAuthorization) {
      return next.handle(request);
    }

    if (!this.auth.getAccessToken()) {
      this.router.navigate(['/login']);
      return throwError('request sent without token');
    }

    request = this.prepareToken(request);

    return next.handle(request)
      .pipe(catchError((err) => {
      return this.handleErrors(err, request, next);
    }));
  }

  private handleErrors(err: { status: number },
                       request: HttpRequest<any>,
                       next: HttpHandler): Observable<HttpEvent<any>> {
    if (err.status === 401) {
      return this.auth.refreshAndValidateToken().pipe(flatMap(() => {
        request = this.prepareToken(request);

        return next.handle(request);
      }));
    }

    return throwError(err);
  }

  private prepareToken(request: HttpRequest<any>): HttpRequest<any> {
    const accessToken = this.auth.getAccessToken();

    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });
  }
}
