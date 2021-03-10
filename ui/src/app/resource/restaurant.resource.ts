import {Resource} from './base/resource';
import {User} from './user.resource';
import {Review} from './review.resource';

export interface Restaurant extends Resource {
  id: number;
  name: string;
  owner: User;
  rating: number;
  highestRatedReview: Review;
  lowestRatedReview: Review;
}
