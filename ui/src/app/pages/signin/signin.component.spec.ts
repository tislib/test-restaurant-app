import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SigninComponent} from './signin.component';
import {AuthenticationService} from '../../service/authentication-service';
import {NavigationExtras, Router} from '@angular/router';
import {Observable, of, throwError} from 'rxjs';
import {TokenDetailsResource} from '../../resource/authentication/token-details.resource';
import {TokenUserDetails} from '../../resource/authentication/token-user-details.resource';
import {NotifierService} from 'angular-notifier';
import {TokenPair} from '../../resource/authentication/token-pair.resource';
import {TokenCreateRequest} from '../../resource/authentication/token-create-request.resource';

describe('SigninComponent', () => {
  let component: SigninComponent;
  let fixture: ComponentFixture<SigninComponent>;

  const mockAuthenticationService: AuthenticationService = {
    getToken: () => of({} as TokenUserDetails),
    createToken(request: TokenCreateRequest): Observable<TokenPair> {
      return of({} as TokenPair);
    },
    setCurrentToken(tokenPair: TokenPair) {

    }
  } as AuthenticationService;

  const mockRouter: Router = {
    navigate: (commands: any[], extras?: NavigationExtras) => new Promise<boolean>(() => true)
  } as Router;

  const mockNotifierService: NotifierService = {
    notify(type: string, message: string, notificationId?: string) {
    }
  } as NotifierService;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [SigninComponent],
      providers: [
        {provide: AuthenticationService, useValue: mockAuthenticationService},
        {provide: NotifierService, useValue: mockNotifierService},
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

  describe('[init]', () => {
    it('should init and initialize service successfully', () => {
      // Arrange
      const tokenUserDetails: TokenUserDetails = {
        user: {
          id: 123
        }
      } as TokenUserDetails;

      spyOn(mockAuthenticationService, 'getToken').and.returnValue(of(tokenUserDetails));
      spyOn(mockNotifierService, 'notify');
      spyOn(mockRouter, 'navigate');

      // Act
      component.ngOnInit();

      // Assert
      expect(mockAuthenticationService.getToken).toHaveBeenCalled();
      expect(mockNotifierService.notify).toHaveBeenCalledOnceWith('success', 'you are authenticated successfully');
      expect(mockRouter.navigate).toHaveBeenCalledOnceWith(['/']);
    });

    it('should init', () => {
      // Arrange
      spyOn(mockAuthenticationService, 'getToken').and.returnValue(throwError('error'));
      spyOn(mockNotifierService, 'notify');
      spyOn(mockRouter, 'navigate');

      // Act
      component.ngOnInit();

      // Assert
      expect(mockAuthenticationService.getToken).toHaveBeenCalled();
      expect(mockNotifierService.notify).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });
  });

  describe('[login]', () => {
    it('should login successfully and redirect to index page', () => {
      // Arrange
      const tokenPair = {
        accessToken: {
          content: 'sample-access-token-123'
        }
      } as TokenPair;

      spyOn(mockAuthenticationService, 'createToken').and.returnValue(of(tokenPair));
      spyOn(mockAuthenticationService, 'setCurrentToken');
      spyOn(mockNotifierService, 'notify');
      spyOn(mockRouter, 'navigate');

      component.email = 'user1@example.com';
      component.password = 'password1';

      // Act
      component.login();

      // Assert
      expect(mockAuthenticationService.createToken).toHaveBeenCalledOnceWith({
        email: 'user1@example.com',
        password: 'password1'
      });
      expect(mockAuthenticationService.setCurrentToken).toHaveBeenCalledOnceWith(tokenPair);
      expect(mockRouter.navigate).toHaveBeenCalled();
    });
    it('should try login with wrong credentials and get error notification', () => {
      // Arrange

      spyOn(mockAuthenticationService, 'createToken').and.returnValue(throwError(''));
      spyOn(mockAuthenticationService, 'setCurrentToken');
      spyOn(mockNotifierService, 'notify');
      spyOn(mockRouter, 'navigate');

      component.email = 'user1@example.com';
      component.password = 'password1';

      // Act
      component.login();

      // Assert
      expect(mockAuthenticationService.createToken).toHaveBeenCalledOnceWith({
        email: 'user1@example.com',
        password: 'password1'
      });
      expect(mockAuthenticationService.setCurrentToken).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
      expect(mockNotifierService.notify).toHaveBeenCalledOnceWith('error', 'username or password is wrong');
    });
  });
});
