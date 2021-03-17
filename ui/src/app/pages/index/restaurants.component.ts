import {Component, OnInit} from '@angular/core';
import {RestaurantService} from '../../service/restaurant-service';
import {PageContainer} from '../../resource/base/page-container';
import {Restaurant} from '../../resource/restaurant.resource';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {RestaurantFormComponent} from '../../components/restaurant-form/restaurant-form.component';
import {faEdit, faTimes, faPlus, faUtensils} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-restaurants',
  templateUrl: './restaurants.component.html',
  styleUrls: ['./restaurants.component.scss']
})
export class RestaurantsComponent implements OnInit {

  editIcon = faEdit;
  deleteIcon = faTimes;
  addIcon = faPlus;
  restaurantIcon = faUtensils;

  public restaurantPagedData?: PageContainer<Restaurant>;

  constructor(private restaurantService: RestaurantService, private ngbModal: NgbModal) {
  }

  ngOnInit(): void {
    this.load();
  }

  private load(): void {
    this.restaurantService.list().subscribe(resp => {
      this.restaurantPagedData = resp;
    });
  }

  add(): void {
    const ref = this.ngbModal.open(RestaurantFormComponent);
    ref.componentInstance.create = true;

    ref.closed.subscribe(() => {
      this.load();
    });
  }

  edit(restaurantId: number): void {
    this.restaurantService.get(restaurantId).subscribe(item => {
      const ref = this.ngbModal.open(RestaurantFormComponent);
      ref.componentInstance.create = false;
      ref.componentInstance.restaurant = item;

      ref.closed.subscribe(() => {
        this.load();
      });
    });
  }

  delete(id: number): void {
    if (confirm('are you want to delete restaurant: ' + id)) {
      this.restaurantService.delete(id).subscribe(() => {
        this.load();
      }, err => {
        alert(err.error.message);
      });
    }
  }
}
