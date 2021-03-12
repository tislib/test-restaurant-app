import {Resource} from './base/resource';
import {User} from './user.resource';
import {OwnerReply} from './owner-reply.resource';

export interface Review extends Resource {
  id: number;
  user: User;
  starCount: number;
  comment: string;
  reviewTime: string | Date;
  dateOfVisit: string | Date;
  ownerReply: OwnerReply;
}
