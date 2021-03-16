import {TokenDetailsResource} from './token-details.resource';

export interface TokenPair {
  accessToken: TokenDetailsResource;
  refreshToken: TokenDetailsResource;
}

