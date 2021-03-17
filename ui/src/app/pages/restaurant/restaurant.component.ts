import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {RestaurantService} from '../../service/restaurant-service';
import {Restaurant} from '../../resource/restaurant.resource';
import {combineLatest} from 'rxjs';
import {ReviewService} from '../../service/review-service';
import {PageContainer} from '../../resource/base/page-container';
import {Review} from '../../resource/review.resource';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ReviewModalFormComponent} from '../../components/review-modal-form/review-modal-form.component';
import {ReviewReplyFormComponent} from '../../components/review-reply-form/review-reply-form.component';
import {OwnerReply} from '../../resource/owner-reply.resource';
import {AuthenticationService} from '../../service/authentication-service';
import {faEdit, faPlus, faReply, faTimes, faUtensils} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.scss']
})
export class RestaurantComponent implements OnInit {

  editIcon = faEdit;
  deleteIcon = faTimes;
  addIcon = faPlus;
  replyIcon = faReply;
  restaurantIcon = faUtensils;

  public restaurant?: Restaurant;
  private restaurantId?: number;
  public reviewPagedData: PageContainer<Review> = {} as PageContainer<Review>;
  public page = 0;
  private pageSize = 25;
  public isUserOwner = false;

  constructor(private route: ActivatedRoute,
              private service: RestaurantService,
              private authenticationService: AuthenticationService,
              private reviewService: ReviewService,
              private ngbModal: NgbModal) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.restaurantId = params.id;
      this.load();
    });
  }

  public load(): void {
    combineLatest([this.service.get(this.restaurantId as number),
      this.reviewService.list(this.restaurantId as number, this.pageSize, 0),
      this.authenticationService.getToken()])
      .subscribe(([restaurant, reviewPagedData, token]) => {
        this.restaurant = restaurant;
        this.reviewPagedData = reviewPagedData;
        this.isUserOwner = token.user.id === restaurant.owner.id;
      });
  }

  loadMore(): void {
    this.page++;

    this.reviewService.list(this.restaurantId as number, this.pageSize, this.page)
      .subscribe((reviewPagedData) => {
        this.reviewPagedData.content = [...this.reviewPagedData.content, ...reviewPagedData.content];
      });
  }

  edit(restaurantId: number, reviewId: number): void {
    this.reviewService.get(restaurantId, reviewId).subscribe(item => {
      const ref = this.ngbModal.open(ReviewModalFormComponent);
      ref.componentInstance.restaurantId = restaurantId;
      ref.componentInstance.create = false;
      ref.componentInstance.review = item;

      ref.closed.subscribe(() => {
        this.load();
      });
    });
  }

  reply(restaurantId: number, reviewId: number): void {
    this.reviewService.get(restaurantId, reviewId).subscribe(item => {
      if (!item.ownerReply) {
        item.ownerReply = {} as OwnerReply;
      }

      const ref = this.ngbModal.open(ReviewReplyFormComponent);
      ref.componentInstance.restaurantId = restaurantId;
      ref.componentInstance.reviewId = reviewId;
      ref.componentInstance.ownerReply = item.ownerReply;

      ref.closed.subscribe(() => {
        this.load();
      });
    });
  }

  delete(restaurantId: number, reviewId: number): void {
    if (confirm('are you want to delete review: ' + reviewId)) {
      this.reviewService.delete(restaurantId, reviewId).subscribe(() => {
        this.load();
      }, err => {
        alert(err.error.message);
      });
    }
  }
}
