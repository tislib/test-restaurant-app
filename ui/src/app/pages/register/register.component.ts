import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../service/authentication-service';
import {Router} from '@angular/router';
import {UserRegistrationRequest} from '../../resource/authentication/user-registration-request.resource';
import {FieldError} from '../../resource/error-response';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  public request: UserRegistrationRequest = {} as UserRegistrationRequest;
  public errors: Map<string, string> = new Map<string, string>();

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  ngOnInit(): void {
  }

  register(): void {
    this.errors.clear();

    this.authenticationService.register(this.request).subscribe(() => {
      this.authenticationService.createToken({
        email: this.request.email,
        password: this.request.password
      }).subscribe((resp) => {
        this.authenticationService.setCurrentToken(resp);
        this.checkAuthentication();
      });
    }, (err) => {
      err.error.rejectedFields.forEach((item: FieldError) => {
        this.errors.set(item.name, item.message);
      });
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
