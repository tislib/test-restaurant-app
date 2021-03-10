import {Resource} from './base/resource';

export interface User extends Resource {
  id: number;
  totalPages: string;
  role: UserRole;
  password: string;
}

export enum UserRole {
  REGULAR = 'REGULAR',
  OWNER = 'OWNER',
  ADMIN = 'ADMIN'
}
