import {HttpClient} from '@angular/common/http';
import {TokenCreateRequest} from '../resource/authentication/token-create-request.resource';
import {Observable} from 'rxjs';
import {TokenPair} from '../resource/authentication/token-pair.resource';
import {API_AUTHENTICATION, PATH_REGISTER, PATH_TOKEN} from '../const/paths';
import {TokenUserDetails} from '../resource/authentication/token-user-details.resource';
import {TokenRefreshRequest} from '../resource/authentication/token-refresh-request.resource';
import {UserRegistrationRequest} from '../resource/authentication/user-registration-request.resource';
import {User} from '../resource/user.resource';
import {Injectable} from '@angular/core';
import {TokenDetailsResource} from '../resource/authentication/token-details.resource';
import {Router} from '@angular/router';

@Injectable()
export class AuthenticationService {

  public constructor(private httpClient: HttpClient, private router: Router) {
  }

  public createToken(request: TokenCreateRequest): Observable<TokenPair> {
    return this.httpClient.post<TokenPair>(API_AUTHENTICATION + PATH_TOKEN, request);
  }

  public getToken(): Observable<TokenUserDetails> {
    return this.httpClient.get<TokenUserDetails>(API_AUTHENTICATION + PATH_TOKEN);
  }

  public refreshToken(request: TokenRefreshRequest): Observable<TokenDetailsResource> {
    return this.httpClient.patch<TokenDetailsResource>(API_AUTHENTICATION + PATH_TOKEN, request);
  }

  public register(request: UserRegistrationRequest): Observable<User> {
    return this.httpClient.patch<User>(API_AUTHENTICATION + PATH_REGISTER, request);
  }

  getAccessToken(): string {
    return localStorage.getItem('access_token') as string;
  }

  setCurrentToken(tokenPair: TokenPair): void {
    localStorage.setItem('access_token', tokenPair.accessToken.content);
    localStorage.setItem('access_token_expiry', tokenPair.accessToken.expiry as string);

    localStorage.setItem('refresh_token', tokenPair.refreshToken.content);
    localStorage.setItem('refresh_token_expiry', tokenPair.refreshToken.expiry as string);
  }

  clearToken(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('access_token_expiry');

    localStorage.removeItem('refresh_token');
    localStorage.removeItem('refresh_token_expiry');
  }

  validateToken(): void {
    const refreshToken = localStorage.getItem('refresh_token') as string;

    if (!refreshToken) {
      this.cleanupAndRedirectToLogin();
    }

    this.refreshToken({
      refreshToken
    }).subscribe((tokenDetails) => {
      // ok
      localStorage.setItem('access_token', tokenDetails.content);
      localStorage.setItem('access_token_expiry', tokenDetails.expiry as string);
    }, () => {
      this.cleanupAndRedirectToLogin();
    });


  }

  private cleanupAndRedirectToLogin(): void {
    this.clearToken();
    this.router.navigate(['/login']);
  }
}
