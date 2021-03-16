import {Component, OnInit} from '@angular/core';
import {Restaurant} from '../../resource/restaurant.resource';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {RestaurantService} from '../../service/restaurant-service';
import {FieldError} from '../../resource/error-response';
import {AuthenticationService} from '../../service/authentication-service';

@Component({
  selector: 'app-restaurant-form',
  templateUrl: './restaurant-form.component.html',
  styleUrls: ['./restaurant-form.component.scss']
})
export class RestaurantFormComponent implements OnInit {

  public restaurant: Restaurant = {
    owner: {}
  } as Restaurant;
  public create = false;
  public errors: Map<string, string> = new Map<string, string>();

  constructor(public activeModal: NgbActiveModal, private service: RestaurantService, private authService: AuthenticationService) {
    authService.getToken().subscribe(token => {
      this.restaurant.owner = token.user;
    });
  }

  ngOnInit(): void {
  }

  save(): void {
    this.errors.clear();

    let api$;
    if (this.create) {
      api$ = this.service.create(this.restaurant);
    } else {
      api$ = this.service.update(this.restaurant.id, this.restaurant);
    }

    api$.subscribe(() => {
      this.activeModal.close();
    }, err => {
      err.error.rejectedFields.forEach((item: FieldError) => {
        this.errors.set(item.name, item.message);
      });
    });
  }
}
