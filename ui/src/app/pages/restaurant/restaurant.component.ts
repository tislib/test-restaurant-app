import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {RestaurantService} from '../../service/restaurant-service';
import {Restaurant} from '../../resource/restaurant.resource';
import {concatAll} from 'rxjs/operators';
import {combineLatest} from 'rxjs';
import {ReviewService} from '../../service/review-service';
import {PageContainer} from '../../resource/base/page-container';
import {Review} from '../../resource/review.resource';

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.scss']
})
export class RestaurantComponent implements OnInit {
  public restaurant?: Restaurant;
  private restaurantId?: number;
  public reviewPagedData: PageContainer<Review> = {} as PageContainer<Review>;
  public page = 0;
  private pageSize = 25;

  constructor(private route: ActivatedRoute, private service: RestaurantService, private reviewService: ReviewService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.restaurantId = params.id;
      this.load();
    });
  }

  public load(): void {
    combineLatest([this.service.get(this.restaurantId as number),
      this.reviewService.list(this.restaurantId as number, this.pageSize, 0)])
      .subscribe(([restaurant, reviewPagedData]) => {
        this.restaurant = restaurant;
        this.reviewPagedData = reviewPagedData;
      });
  }

  loadMore(): void {
    this.page++;

    this.reviewService.list(this.restaurantId as number, this.pageSize, this.page)
      .subscribe((reviewPagedData) => {
        this.reviewPagedData.content = [...this.reviewPagedData.content, ...reviewPagedData.content];
      });
  }
}
