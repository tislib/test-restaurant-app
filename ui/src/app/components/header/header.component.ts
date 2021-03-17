import {Component, OnInit} from '@angular/core';
import {TokenUserDetails} from '../../resource/authentication/token-user-details.resource';
import {AuthenticationService} from '../../service/authentication-service';
import {Router} from '@angular/router';
import {NotifierService} from 'angular-notifier';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  public tokenDetails?: TokenUserDetails;

  constructor(private authService: AuthenticationService, private notifierService: NotifierService) {
  }

  ngOnInit(): void {
    this.authService.getToken().subscribe(tokenDetails => {
      this.tokenDetails = tokenDetails;
    });
  }

  logout(): void {
    this.authService.logout();
    this.notifierService.notify('success', 'you are logged out successfully');
  }
}
