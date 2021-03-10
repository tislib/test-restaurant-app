import {User} from '../user.resource';

export interface TokenUserDetails {
  creationTime: string | Date;
  expirationTime: string | Date;
  user: User;
}
