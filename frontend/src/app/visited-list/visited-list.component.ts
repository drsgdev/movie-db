import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-visited-list',
  templateUrl: './visited-list.component.html',
  styleUrls: ['./visited-list.component.scss'],
})
export class VisitedListComponent implements OnInit {
  visited: any[];

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.db.fetchVisited().subscribe(
      (res) => (this.visited = res),
      (err) => {}
    );
  }
}
