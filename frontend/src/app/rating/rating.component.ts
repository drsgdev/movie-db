import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import {
  map,
  debounceTime,
  distinctUntilChanged,
  tap,
  switchMap,
} from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss'],
  inputs: ['id'],
})
export class RatingComponent implements OnInit {
  id: number;
  ratings: Observable<number[]>;
  relative_ratings = new Array<number>(0, 0, 0, 0, 0);
  total: number = 0;
  average: number = 0;

  isCollapsed = true;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.ratings = this.db.getRating(this.id);

    this.ratings.pipe(
      map((res) => {
        this.total = res.reduce((sum, curr) => sum += curr);
        this.relative_ratings = res.slice();
        this.relative_ratings.forEach((val, index) => {
          this.average += val * (5 - index);
          this.relative_ratings[index] = (val / this.total) * 100;
        }, this);

        this.average = this.average / this.total;
      })
    ).subscribe();
  }

  getFlooredGrade() {
    return Math.floor(this.average);
  }
}
