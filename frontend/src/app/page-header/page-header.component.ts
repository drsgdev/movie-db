import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.scss'],
})
export class PageHeaderComponent implements OnInit {
  isLoggedIn: boolean;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.db.isLoggedIn();
  }

  loggedIn() {
      return this.db.isLoggedIn();
  }

  logout() {
    this.isLoggedIn = false;
    this.db.logout();
  }
}
