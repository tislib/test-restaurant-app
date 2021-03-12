import {Component, OnInit} from '@angular/core';
import {PageContainer} from '../../resource/base/page-container';
import {User} from '../../resource/user.resource';
import {UserService} from '../../service/user-service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  public userPagedData: PageContainer<User> = {} as PageContainer<User>;

  constructor(private userService: UserService) {
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

  edit(id: number): void {

  }

  delete(id: number): void {
    if (confirm('are you want to delete user: ' + id)) {
      this.userService.delete(id).subscribe(() => {
        this.load();
      }, err => {
        alert(err.error.message);
      });
    }
  }
}
