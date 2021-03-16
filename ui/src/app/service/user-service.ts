import {HttpClient} from '@angular/common/http';
import {User} from '../resource/user.resource';
import {API_USER} from '../const/paths';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageContainer} from '../resource/base/page-container';
import {Restaurant} from '../resource/restaurant.resource';

@Injectable()
export class UserService {

  private readonly baseUrl = API_USER;
  private readonly byIdUrl = (id: number) => this.baseUrl + '/' + id;


  public constructor(private httpClient: HttpClient) {
  }

  public create(resource: User): Observable<User> {
    return this.httpClient.post<User>(this.baseUrl, resource);
  }

  public update(id: number, resource: User): Observable<User> {
    return this.httpClient.put<User>(this.byIdUrl(id), resource);
  }

  public list(): Observable<PageContainer<User>> {
    return this.httpClient.get<PageContainer<User>>(this.baseUrl);
  }

  public get(id: number): Observable<User> {
    return this.httpClient.get<User>(this.byIdUrl(id));
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.byIdUrl(id));
  }
}
