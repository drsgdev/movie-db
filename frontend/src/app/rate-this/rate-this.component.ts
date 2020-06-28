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
  selected_paths = [1,1,1,1,1];

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {}

  mouseover(event) {
    this.selected_paths.forEach((val, id) => {
      if (id <= event.target.id - 1) {
        this.selected_paths[id] = 2;
      }
    })
  }

  mouseleave(event) {
    this.selected_paths.forEach((val, id) => {
      if (id <= event.target.id - 1) {
        this.selected_paths[id] = 1;
      }
    })
  }

  rate(event) {
    this.db.rate(this.id, event.target.id);
  }
}
