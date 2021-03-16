import {Resource} from './base/resource';

export interface User extends Resource {
  id: number;
  email: string;
  role: UserRole;
  fullName: string;
  password: string;
}

export enum UserRole {
  REGULAR = 'REGULAR',
  OWNER = 'OWNER',
  ADMIN = 'ADMIN'
}
