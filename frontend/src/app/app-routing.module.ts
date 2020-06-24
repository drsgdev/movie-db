import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CelebsPageComponent } from './celebs-page/celebs-page.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { MoviesPageComponent } from './movies-page/movies-page.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ShowsPageComponent } from './shows-page/shows-page.component';
import { ContentPageComponent } from './content-page/content-page.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'movies', component: MoviesPageComponent },
  { path: 'shows', component: ShowsPageComponent },
  { path: 'celebs', component: CelebsPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'movie/:id', component: ContentPageComponent },
  { path: 'show/:id', component: ContentPageComponent },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
