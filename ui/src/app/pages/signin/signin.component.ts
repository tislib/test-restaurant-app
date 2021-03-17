import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../service/authentication-service';
import {Router} from '@angular/router';
import {NotifierService} from 'angular-notifier';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  public email = '';
  public password = '';

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private notifierService: NotifierService) {
  }

  ngOnInit(): void {
    this.checkAuthentication();
  }

  login(): void {
    this.authenticationService.createToken({
      email: this.email,
      password: this.password
    }).subscribe(resp => {
      this.authenticationService.setCurrentToken(resp);
      this.checkAuthentication();
    }, () => {
      this.notifierService.notify('error', 'username or password is wrong');
    });
  }

  private checkAuthentication(): void {
    this.authenticationService.getToken()
      .subscribe(() => {
        this.notifierService.notify('success', 'you are authenticated successfully');
        this.router.navigate(['/']);
      });
  }
}
