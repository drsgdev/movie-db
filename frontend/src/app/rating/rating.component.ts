import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss'],
  inputs: ['ratings'],
})
export class RatingComponent implements OnInit {
  ratings: [];
  relative_ratings = new Array<number>();
  total: number = 0;
  average: number = 0;

  constructor() {}

  ngOnInit(): void {
    this.total = this.ratings
      .map((val) => {
        let actual = Number.parseInt(val);
        this.relative_ratings.push(actual);

        return actual;
      })
      .reduce((sum, current) => (sum += current));

    this.relative_ratings.forEach((val, index) => {
      this.average += val * (5 - index);
      this.relative_ratings[index] = (val / this.total) * 100;
    }, this);

    this.average = this.average / this.total;
  }

  getFlooredGrade() {
    return Math.floor(this.average);
  }
}
