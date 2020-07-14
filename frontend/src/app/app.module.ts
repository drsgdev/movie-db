import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { PageHeaderComponent } from './page-header/page-header.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { MoviesPageComponent } from './movies-page/movies-page.component';
import { ShowsPageComponent } from './shows-page/shows-page.component';
import { CelebsPageComponent } from './celebs-page/celebs-page.component';
import { ContentSliderComponent } from './content-slider/content-slider.component';
import { ContentPageComponent } from './content-page/content-page.component';
import { RatingComponent } from './rating/rating.component';
import { RateThisComponent } from './rate-this/rate-this.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReviewsListComponent } from './reviews-list/reviews-list.component';
import { SignupPageComponent } from './signup-page/signup-page.component';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { InterceptorService } from './interceptor.service';
import { PersonPageComponent } from './person-page/person-page.component';
import { ProfilePageComponent } from './profile-page/profile-page.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    PageHeaderComponent,
    NotFoundComponent,
    LoginPageComponent,
    MoviesPageComponent,
    ShowsPageComponent,
    CelebsPageComponent,
    ContentSliderComponent,
    ContentPageComponent,
    RatingComponent,
    RateThisComponent,
    ReviewsListComponent,
    SignupPageComponent,
    PersonPageComponent,
    ProfilePageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    NgxWebstorageModule.forRoot(),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
