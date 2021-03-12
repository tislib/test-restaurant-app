import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Review} from '../../resource/review.resource';
import {ReviewService} from '../../service/review-service';
import {FieldError} from '../../resource/error-response';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-review-modal-form',
  templateUrl: './review-modal-form.component.html',
  styleUrls: ['./review-modal-form.component.scss']
})
export class ReviewModalFormComponent implements OnInit {

  public review: Review = {} as Review;
  public errors: Map<string, string> = new Map<string, string>();

  public restaurantId = 0;

  constructor(private service: ReviewService, public activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
  }

  save(): void {
    this.errors.clear();
    this.service.update(this.restaurantId, this.review.id, this.review).subscribe(() => {
      this.activeModal.close();
    }, err => {
      err.error.rejectedFields.forEach((item: FieldError) => {
        this.errors.set(item.name, item.message);
      });
    });
  }
}
