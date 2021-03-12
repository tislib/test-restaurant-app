import {Component, OnInit} from '@angular/core';
import {PageContainer} from '../../resource/base/page-container';
import {User} from '../../resource/user.resource';
import {UserService} from '../../service/user-service';
import {UserFormComponent} from '../../components/user-form/user-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  public userPagedData: PageContainer<User> = {} as PageContainer<User>;

  constructor(private userService: UserService, private ngbModal: NgbModal) {
  }

  ngOnInit(): void {
    this.load();
  }

  private load(): void {
    this.userService.list()
      .subscribe((data) => {
        this.userPagedData = data;
      });
  }

  add(): void {
    const ref = this.ngbModal.open(UserFormComponent);
    ref.componentInstance.create = true;

    ref.closed.subscribe(() => {
      this.load();
    });
  }

  edit(restaurantId: number): void {
    this.userService.get(restaurantId).subscribe(item => {
      const ref = this.ngbModal.open(UserFormComponent);
      ref.componentInstance.create = false;
      ref.componentInstance.user = item;

      ref.closed.subscribe(() => {
        this.load();
      });
    });
  }

  delete(id: number): void {
    if (confirm('are you want to delete restaurant: ' + id)) {
      this.userService.delete(id).subscribe(() => {
        this.load();
      }, err => {
        alert(err.error.message);
      });
    }
  }
}
