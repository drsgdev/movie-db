import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.scss'],
})
export class PageHeaderComponent implements OnInit {
  isAdmin: boolean;
  roleSubscription: Subscription;

  loginSubscription: Subscription;
  isLoggedIn: boolean;

  constructor(private db: DatabaseService, private router: Router) {
    this.roleSubscription = this.db.isAdmin$.subscribe(
      (res) => (this.isAdmin = res)
    );
    this.loginSubscription = this.db.isLoggedIn$.subscribe((res) => {
      this.isLoggedIn = res;
    });
  }

  ngOnInit(): void {
    this.db.updateLoginState();
    this.db.updateRole();
  }

  logout() {
    this.db.logout();
    this.router.navigateByUrl('/');
  }

  username() {
    return this.db.getUsername();
  }
}
