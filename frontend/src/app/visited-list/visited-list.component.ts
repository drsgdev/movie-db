import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-visited-list',
  templateUrl: './visited-list.component.html',
  styleUrls: ['./visited-list.component.scss'],
})
export class VisitedListComponent implements OnInit {
  visited: any[];

  loginSubscription: Subscription;
  isLoggedIn: boolean;

  constructor(private db: DatabaseService) {
    this.loginSubscription = this.db.isLoggedIn$.subscribe((res) => {
      this.isLoggedIn = res;
    });
  }

  ngOnInit(): void {
    this.db.updateLoginState();
    this.db.fetchVisited().subscribe(
      (res) => (this.visited = res),
      (err) => {}
    );
  }
}
