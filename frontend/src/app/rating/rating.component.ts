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
  relative_ratings = new Array<number>();
  total: number = 0;
  average: number = 0;

  isCollapsed = true;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.db.getRating(this.id).subscribe((res) => {
      console.log(res);
      this.ratings = <number[]>res;
      this.total = this.ratings
        .map((val) => {
          this.relative_ratings.push(val);

          return val;
        })
        .reduce((sum, current) => (sum += current));
    }, (err) => {
      this.ratings = new Array<number>();
    });

    if (this.ratings != undefined) {
      this.relative_ratings.forEach((val, index) => {
        this.average += val * (5 - index);
        this.relative_ratings[index] = (val / this.total) * 100;
      }, this);

      this.average = this.average / this.total;
    }
  }

  getFlooredGrade() {
    return Math.floor(this.average);
  }
}
