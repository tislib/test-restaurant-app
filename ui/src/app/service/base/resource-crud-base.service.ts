import {HttpClient, HttpParams} from '@angular/common/http';
import {Resource} from '../../resource/base/resource';
import {Observable} from 'rxjs';
import {PageContainer} from '../../resource/base/page-container';

export abstract class ResourceCrudBaseService<T extends Resource> {
  protected constructor(private baseUrl: string, private httpClient: HttpClient) {
  }

  public create(resource: T): Observable<T> {
    return this.httpClient.post<T>(this.baseUrl, resource);
  }

  public list(params: HttpParams | {
    [param: string]: string | string[];
  }): Observable<PageContainer<T>> {
    return this.httpClient.get<PageContainer<T>>(this.baseUrl, {
      params
    });
  }

}
