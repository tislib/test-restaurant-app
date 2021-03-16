import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../service/authentication-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  public email = '';
  public password = '';

  constructor(private authenticationService: AuthenticationService,
              private router: Router) {
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
      alert('username or password is invalid');
    });
  }

  private checkAuthentication(): void {
    this.authenticationService.getToken()
      .subscribe(() => {
        alert('authenticated');
        this.router.navigate(['/']);
      });
  }
}
