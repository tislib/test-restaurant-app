import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {RestaurantsComponent} from './pages/index/restaurants.component';
import {FooterComponent} from './components/footer/footer.component';
import {SigninComponent} from './pages/signin/signin.component';
import {RegisterComponent} from './pages/register/register.component';
import {RestaurantComponent} from './pages/restaurant/restaurant.component';
import {HeaderComponent} from './components/header/header.component';
import {HeaderMobileComponent} from './components/header-mobile/header-mobile.component';
import {LayoutComponent} from './components/layout/layout.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {TokenInterceptor} from './interceptor/token-interceptor';
import {AuthenticationService} from './service/authentication-service';
import {RestaurantService} from './service/restaurant-service';
import {ReviewService} from './service/review-service';
import {ReviewFormComponent} from './components/review-form/review-form.component';
import {RestaurantFormComponent} from './components/restaurant-form/restaurant-form.component';
import {UserFormComponent} from './components/user-form/user-form.component';
import {UsersComponent} from './pages/users/users.component';
import {UserService} from './service/user-service';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ReviewModalFormComponent} from './components/review-modal-form/review-modal-form.component';
import { ReviewReplyFormComponent } from './components/review-reply-form/review-reply-form.component';
import { IfHasRoleDirective } from './directives/if-has-role.directive';
import {NotifierModule} from 'angular-notifier';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    RestaurantsComponent,
    FooterComponent,
    SigninComponent,
    LayoutComponent,
    RegisterComponent,
    RestaurantComponent,
    HeaderMobileComponent,
    ReviewFormComponent,
    RestaurantFormComponent,
    UserFormComponent,
    UsersComponent,
    ReviewModalFormComponent,
    ReviewReplyFormComponent,
    IfHasRoleDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    NotifierModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    AuthenticationService,
    RestaurantService,
    ReviewService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
