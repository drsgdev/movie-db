import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-content-page',
  templateUrl: './content-page.component.html',
  styleUrls: ['./content-page.component.scss'],
  inputs: ['id'],
})
export class ContentPageComponent implements OnInit {
  id = 0;
  content: any;

  constructor(private db: DatabaseService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });
    // this.content = this.db.fetchById(this.id);
    this.content = this.db.getById(this.id - 1);
  }

  getRatings() {
    let ratings = [];

    Object.keys(this.content.ratings).map((key) => {
      ratings.push(this.content.ratings[key]);
    });

    return ratings;
  }
}
