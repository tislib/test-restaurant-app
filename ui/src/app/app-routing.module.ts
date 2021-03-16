import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RestaurantsComponent} from './pages/index/restaurants.component';
import {SigninComponent} from './pages/signin/signin.component';
import {RestaurantComponent} from './pages/restaurant/restaurant.component';
import {UsersComponent} from './pages/users/users.component';
import {RegisterComponent} from './pages/register/register.component';

const routes: Routes = [
  {
    path: 'restaurants',
    component: RestaurantsComponent
  },
  {
    path: 'login',
    component: SigninComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'users',
    component: UsersComponent
  },
  {
    path: 'restaurant/:id',
    component: RestaurantComponent
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'restaurants'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
