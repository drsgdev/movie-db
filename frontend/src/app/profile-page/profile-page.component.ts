import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.scss'],
})
export class ProfilePageComponent implements OnInit {
  user: any;
  username: string;

  favorites: any[];
  rated: any[];
  reviewed: any[];

  constructor(
    private db: DatabaseService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    route.params.subscribe((params) => {
      this.username = params['username'];
      this.updateInfo();
    });
  }

  ngOnInit(): void {}

  loggedIn() {
    return this.db.isLoggedIn();
  }

  updateInfo() {
    this.db.fetchUser(this.username).subscribe(
      (res) => (this.user = res),
      (err) => this.router.navigateByUrl('/404')
    );

    this.db.getFavorites(this.username).subscribe(
      (res) => (this.favorites = res),
      (err) => {}
    );

    this.db.getRated(this.username).subscribe(
      (res) => (this.rated = res),
      (err) => {}
    );

    this.db.getReviewed(this.username).subscribe(
      (res) => (this.reviewed = res),
      (err) => {}
    );
  }
}
