import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Review} from '../../resource/review.resource';
import {ReviewService} from '../../service/review-service';
import {FieldError} from '../../resource/error-response';
import {NgbActiveModal, NgbDate} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-review-modal-form',
  templateUrl: './review-modal-form.component.html',
  styleUrls: ['./review-modal-form.component.scss']
})
export class ReviewModalFormComponent implements OnInit {

  public review: Review = {} as Review;
  public errors: Map<string, string> = new Map<string, string>();

  public restaurantId = 0;
  public dateOfVisit: NgbDate = {} as NgbDate;

  constructor(private service: ReviewService, public activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
    const date = new Date(this.review.dateOfVisit);

    this.dateOfVisit = {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate()
    } as NgbDate;
  }

  save(): void {
    this.errors.clear();

    this.review.dateOfVisit = this.ngbDateToString(this.dateOfVisit);

    this.service.update(this.restaurantId, this.review.id, this.review).subscribe(() => {
      this.activeModal.close();
    }, err => {
      err.error.rejectedFields.forEach((item: FieldError) => {
        this.errors.set(item.name, item.message);
      });
    });
  }

  private ngbDateToString(ngbDate: NgbDate): string {
    return String(ngbDate.year).padStart(4, '0') + '-' + String(ngbDate.month).padStart(2, '0') + '-' + String(ngbDate.day).padStart(2, '0');
  }
}
