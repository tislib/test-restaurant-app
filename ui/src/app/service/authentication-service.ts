import {HttpClient} from '@angular/common/http';
import {TokenCreateRequest} from '../resource/authentication/token-create-request.resource';
import {Observable} from 'rxjs';
import {TokenPair} from '../resource/authentication/token-pair.resource';
import {API_AUTHENTICATE, PATH_REGISTER, PATH_TOKEN} from '../const/paths';
import {TokenUserDetails} from '../resource/authentication/token-user-details.resource';
import {TokenRefreshRequest} from '../resource/authentication/token-refresh-request.resource';
import {UserRegistrationRequest} from '../resource/authentication/user-registration-request.resource';
import {User} from '../resource/user.resource';

export class AuthenticationService {
  public constructor(private httpClient: HttpClient) {
  }

  public createToken(request: TokenCreateRequest): Observable<TokenPair> {
    return this.httpClient.post<TokenPair>(API_AUTHENTICATE + PATH_TOKEN, request);
  }

  public getToken(): Observable<TokenUserDetails> {
    return this.httpClient.get<TokenUserDetails>(API_AUTHENTICATE + PATH_TOKEN);
  }

  public refreshToken(request: TokenRefreshRequest): Observable<TokenUserDetails> {
    return this.httpClient.patch<TokenUserDetails>(API_AUTHENTICATE + PATH_TOKEN, request);
  }

  public register(request: UserRegistrationRequest): Observable<User> {
    return this.httpClient.patch<User>(API_AUTHENTICATE + PATH_REGISTER, request);
  }

}
