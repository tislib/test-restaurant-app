import {Component, OnInit} from '@angular/core';
import {TokenUserDetails} from '../../resource/authentication/token-user-details.resource';
import {AuthenticationService} from '../../service/authentication-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  public tokenDetails?: TokenUserDetails;

  constructor(private authService: AuthenticationService, private router: Router) {
  }

  ngOnInit(): void {
    this.authService.getToken().subscribe(tokenDetails => {
      this.tokenDetails = tokenDetails;
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
