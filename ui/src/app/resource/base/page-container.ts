import {Resource} from './resource';

export interface PageContainer<T extends Resource> {
  content: T[];
  totalElements: number;
  totalPages: number;
}
