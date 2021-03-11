import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {IndexComponent} from './pages/index/index.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    IndexComponent,
    FooterComponent,
    SigninComponent,
    LayoutComponent,
    RegisterComponent,
    RestaurantComponent,
    HeaderMobileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    AuthenticationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
