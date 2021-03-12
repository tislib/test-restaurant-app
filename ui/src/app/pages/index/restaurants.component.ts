import { Component, OnInit } from '@angular/core';
import {RestaurantService} from '../../service/restaurant-service';
import {PageContainer} from '../../resource/base/page-container';
import {Restaurant} from '../../resource/restaurant.resource';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {RestaurantFormComponent} from '../../components/restaurant-form/restaurant-form.component';

@Component({
  selector: 'app-index',
  templateUrl: './restaurants.component.html',
  styleUrls: ['./restaurants.component.scss']
})
export class RestaurantsComponent implements OnInit {

  public restaurantPagedData?: PageContainer<Restaurant>;

  constructor(private restaurantService: RestaurantService, private ngbModal: NgbModal) { }

  ngOnInit(): void {
    this.restaurantService.list().subscribe(resp => {
      this.restaurantPagedData = resp;
    });
  }

  addRestaurant(): void {
    this.ngbModal.open(RestaurantFormComponent);
  }
}
