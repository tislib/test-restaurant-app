import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SigninComponent} from './signin.component';
import {AuthenticationService} from '../../service/authentication-service';
import {NavigationExtras, Router} from '@angular/router';
import {Observable, of} from 'rxjs';
import {TokenDetailsResource} from '../../resource/authentication/token-details.resource';
import {TokenUserDetails} from '../../resource/authentication/token-user-details.resource';

describe('SigninComponent', () => {
  let component: SigninComponent;
  let fixture: ComponentFixture<SigninComponent>;

  beforeEach(async () => {
    const mockAuthenticationService: AuthenticationService = {
      getToken: () => of({} as TokenUserDetails)
    } as AuthenticationService;
    const mockRouter: Router = {
      navigate: (commands: any[], extras?: NavigationExtras) => new Promise<boolean>(() => true)
    } as Router;

    await TestBed.configureTestingModule({
      declarations: [SigninComponent],
      providers: [
        {provide: AuthenticationService, useValue: mockAuthenticationService},
        {provide: Router, useValue: mockRouter}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SigninComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
