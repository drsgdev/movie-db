import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-rate-this',
  templateUrl: './rate-this.component.html',
  styleUrls: ['./rate-this.component.scss'],
  inputs: ['id'],
})
export class RateThisComponent implements OnInit {
  id: number;
  current_rate: number;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {}

  rate() {
      this.db.
  }

  loggedIn() {
      this.db.isLoggedIn();
  }
}
