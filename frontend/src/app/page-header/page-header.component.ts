import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.scss'],
})
export class PageHeaderComponent implements OnInit {
  constructor(private db: DatabaseService, private router: Router) {}

  ngOnInit(): void {}

  loggedIn() {
    return this.db.isLoggedIn();
  }

  logout() {
    this.db.logout();
    this.router.navigate(['/']);
  }

  username() {
    return this.db.getUsername();
  }
}
