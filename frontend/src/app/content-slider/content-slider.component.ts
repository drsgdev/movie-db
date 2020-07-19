import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbCarousel, NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-content-slider',
  templateUrl: './content-slider.component.html',
  styleUrls: ['./content-slider.component.scss'],
  inputs: [
    'id',
    'name',
    'dataType: data-type',
    'creditType: credit-type',
    'objectsPerPage: page-size',
  ],
})
export class ContentSliderComponent implements OnInit {
  id: number;
  name: string;

  data: any[];
  dataType: string;
  creditType: string;

  objectsPerPage = 3;
  pages = 1;

  @ViewChild('carousel') carousel: NgbCarousel;

  constructor(private db: DatabaseService, private config: NgbCarouselConfig) {}

  ngOnInit(): void {
    if (this.creditType) {
      this.db
        .fetchCreditsById(this.id, this.dataType, this.creditType)
        .subscribe(
          (res) => {
            this.data = <any[]>res;
            this.pages = Math.ceil(this.data.length / this.objectsPerPage);
          },
          (err) => {}
        );
    } else {
      this.db.fetchAllByType(this.dataType).subscribe(
        (res) => {
          this.data = <any[]>res;
          this.pages = Math.ceil(this.data.length / this.objectsPerPage);
        },
        (err) => {}
      );
    }
  }

  ngAfterViewInit() {
    this.config.interval = -1;
    this.config.showNavigationIndicators = false;
  }

  getObjectPool(pageId: number) {
    let start: number = pageId * this.objectsPerPage;
    let end: number = start + this.objectsPerPage;

    if (end >= this.data.length) {
      end = this.data.length;
    }
    return this.data.slice(start, end);
  }

  isPersonCredits() {
    return this.dataType === 'person';
  }

  isMovieList() {
    return !this.isPersonCredits() && !this.creditType;
  }
}
