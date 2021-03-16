import {Component, OnInit} from '@angular/core';
import {Review} from '../../resource/review.resource';
import {ReviewService} from '../../service/review-service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FieldError} from '../../resource/error-response';
import {OwnerReply} from '../../resource/owner-reply.resource';

@Component({
  selector: 'app-review-reply-form',
  templateUrl: './review-reply-form.component.html',
  styleUrls: ['./review-reply-form.component.scss']
})
export class ReviewReplyFormComponent implements OnInit {

  public reviewId = 0;
  public ownerReply: OwnerReply = {} as OwnerReply;
  public errors: Map<string, string> = new Map<string, string>();

  public restaurantId = 0;

  constructor(private service: ReviewService, public activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
  }

  save(): void {
    this.errors.clear();
    this.service.reply(this.restaurantId, this.reviewId, this.ownerReply).subscribe(() => {
      this.activeModal.close();
    }, err => {
      err.error.rejectedFields.forEach((item: FieldError) => {
        this.errors.set(item.name, item.message);
      });
    });
  }
}
