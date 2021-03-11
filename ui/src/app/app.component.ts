import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from './service/authentication-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'restaurant-app-ui';
  username = '';
  password = '';

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.authenticationService.getToken().subscribe(tokenDetails => {
      console.log(tokenDetails);
    });
  }

}
