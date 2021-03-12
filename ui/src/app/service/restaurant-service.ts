import {HttpClient, HttpParams} from '@angular/common/http';
import {Restaurant} from '../resource/restaurant.resource';
import {API_RESTAURANT, API_REVIEW} from '../const/paths';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageContainer} from '../resource/base/page-container';

@Injectable()
export class RestaurantService {

  private readonly baseUrl = API_RESTAURANT;
  private readonly byIdUrl = (id: number) => this.baseUrl + '/' + id;


  public constructor(private httpClient: HttpClient) {
  }

  public create(resource: Restaurant): Observable<Restaurant> {
    return this.httpClient.post<Restaurant>(this.baseUrl, resource);
  }

  public list(params?: HttpParams | {
    [param: string]: string | string[];
  }): Observable<PageContainer<Restaurant>> {
    return this.httpClient.get<PageContainer<Restaurant>>(this.baseUrl, {
      params
    });
  }

  public get(id: number): Observable<Restaurant> {
    return this.httpClient.get<Restaurant>(this.byIdUrl(id));
  }
}
