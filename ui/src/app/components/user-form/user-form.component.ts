import {Component, OnInit} from '@angular/core';
import {User, UserRole} from '../../resource/user.resource';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UserService} from '../../service/user-service';
import {FieldError} from '../../resource/error-response';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  public user: User = {
    role: UserRole.REGULAR
  } as User;
  public create = false;
  public errors: Map<string, string> = new Map<string, string>();

  constructor(public activeModal: NgbActiveModal, private service: UserService) {
  }

  ngOnInit(): void {
  }

  save(): void {
    this.errors.clear();

    let api$;
    if (this.create) {
      api$ = this.service.create(this.user);
    } else {
      api$ = this.service.update(this.user.id, this.user);
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
