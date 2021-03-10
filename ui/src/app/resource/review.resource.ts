import {Resource} from './base/resource';
import {User} from './user.resource';

export interface Review extends Resource {
  id: number;
  user: User;
  starCount: number;
  comment: string;
  reviewTime: string | Date;
  dateOfVisit: string | Date;
  ownerReply: {
    comment: string
  };
}
