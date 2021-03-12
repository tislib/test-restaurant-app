import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageContainer} from '../resource/base/page-container';
import {API_REVIEW} from '../const/paths';
import {Review} from '../resource/review.resource';

@Injectable()
export class ReviewService {

  private readonly baseUrl = (restaurantId: number) => API_REVIEW.replace('{restaurantId}', String(restaurantId));
  private readonly byIdUrl = (restaurantId: number, id: number) => this.baseUrl(restaurantId) + '/' + id;

  public constructor(private httpClient: HttpClient) {
  }

  public create(restaurantId: number, resource: Review): Observable<Review> {
    return this.httpClient.post<Review>(this.baseUrl(restaurantId), resource);
  }

  public list(restaurantId: number, pageSize: number, page: number): Observable<PageContainer<Review>> {
    return this.httpClient.get<PageContainer<Review>>(this.baseUrl(restaurantId) + `?pageSize=${pageSize}&page=${page}`);
  }

  public get(restaurantId: number, id: number): Observable<Review> {
    return this.httpClient.get<Review>(this.baseUrl(restaurantId) + '/' + id);
  }
}