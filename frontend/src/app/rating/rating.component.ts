import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss'],
  inputs: ['id'],
})
export class RatingComponent implements OnInit {
  id: number;
  ratings: number[];
  relative_ratings: number[];
  total: number;
  average: number;

  isCollapsed = true;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.updateRating();
  }

  getFlooredGrade() {
    return Math.floor(this.average);
  }

  updateRating() {
    this.total = 0;
    this.average = 0;

    this.db.getRating(this.id).subscribe(
      (res) => {
        this.ratings = res;

        this.total = res.reduce((sum, curr) => (sum += curr));
        this.relative_ratings = res.slice();
        this.relative_ratings.forEach((val, index) => {
          this.average += val * (5 - index);
          this.relative_ratings[index] = (val / this.total) * 100;
        }, this);

        this.average = this.average / this.total;
      },
      (err) => {}
    );
  }
}
