import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Review} from '../../resource/review.resource';
import {ReviewService} from '../../service/review-service';
import {FieldError} from '../../resource/error-response';
import {DatepickerViewModel} from '@ng-bootstrap/ng-bootstrap/datepicker/datepicker-view-model';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

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

  public dateOfVisit: NgbDate = {} as NgbDate;

  constructor(private service: ReviewService) {
  }

  ngOnInit(): void {
    const date = new Date();

    this.dateOfVisit = {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate()
    } as NgbDate;
  }

  create(): void {
    this.errors.clear();

    this.review.dateOfVisit = this.ngbDateToString(this.dateOfVisit);

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
    } as Review;
  }

  private ngbDateToString(ngbDate: NgbDate): string {
    return String(ngbDate.year).padStart(4, '0') + '-' + String(ngbDate.month).padStart(2, '0') + '-' + String(ngbDate.day).padStart(2, '0');
  }
}
