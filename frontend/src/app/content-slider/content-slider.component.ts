import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-content-slider',
  templateUrl: './content-slider.component.html',
  styleUrls: ['./content-slider.component.scss'],
  inputs: ['name', 'dataType: data-type', 'objectsPerPage: page-size'],
})
export class ContentSliderComponent implements OnInit {
  name = '';
  dataType = 'movie';
  data: any[];

  objectsPerPage = 3;
  pages = [];
  objects = [];
  currentPage = 1;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    // this.data = this.service.fetchNewByType(this.dataType);

    // initialization is for testing purposes
    console.log(this.dataType);

    switch (this.dataType) {
      case 'cast':
        this.data = this.db.cast;
        break;
      default:
        this.data = this.db.data;
        break;
    }

    console.log(this.data);

    const maxPages = this.data.length / this.objectsPerPage;

    for (let index = 0; index < maxPages; index++) {
      this.pages.push(index);
    }

    for (let index = 0; index < this.objectsPerPage; index++) {
      this.objects.push(index);
    }
  }

  nextPage() {
    this.currentPage++;
  }

  prevPage() {
    this.currentPage--;
  }
}
