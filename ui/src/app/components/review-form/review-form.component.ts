import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Review} from '../../resource/review.resource';
import {ReviewService} from '../../service/review-service';
import {FieldError} from '../../resource/error-response';

@Component({
  selector: 'app-review-form',
  templateUrl: './review-form.component.html',
  styleUrls: ['./review-form.component.scss']
})
export class ReviewFormComponent implements OnInit {

  public review: Review = this.initReview();
  public errors: Map<string, string> = new Map<string, string>();

  @Input()
  public restaurantId = 0;

  @Output()
  public update: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private service: ReviewService) {
  }

  ngOnInit(): void {
  }

  create(): void {
    this.errors.clear();

    this.service.create(this.restaurantId, this.review).subscribe(() => {
      this.review = this.initReview();
      this.update.next();
    }, err => {
      err.error.rejectedFields.forEach((item: FieldError) => {
        this.errors.set(item.name, item.message);
      });
    });
  }

  private initReview(): Review {
    return {
      dateOfVisit: '2021-01-02',
      starCount: 3,
      comment: 'just another dummy comment to make the comment a valid comment'
    } as Review;
  }
}
