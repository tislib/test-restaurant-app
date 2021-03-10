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
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
