import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-content-slider',
  templateUrl: './content-slider.component.html',
  styleUrls: ['./content-slider.component.scss'],
  inputs: ['name', 'dataType: data-type'],
})
export class ContentSliderComponent implements OnInit {
  name = '';
  dataType = 'movie';
  data: any[];

  objectsPerPage = 3;
  pages = [];
  currentPage = 1;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    // this.data = this.service.fetchNewByType(this.dataType);

    // initialization is for testing purposes
    this.data = this.db.getData();

    const maxPages = this.data.length / this.objectsPerPage;

    for (let index = 0; index < maxPages; index++) {
      this.pages.push(index);
    }
  }

  nextPage() {
    this.currentPage++;
  }

  prevPage() {
    this.currentPage--;
  }
}
