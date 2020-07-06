import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { DatabaseService } from '../database.service';
import { NgbCarousel, NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';

// TODO: fix this component!
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

  isActive = true;

  objectsPerPage = 3;
  pages = 1;

  @ViewChild('carousel') carousel: NgbCarousel;

  constructor(private db: DatabaseService, private config: NgbCarouselConfig) {}

  ngOnInit(): void {
    if (this.creditType) {
      this.db
        .fetchCreditsById(this.id, this.dataType, this.creditType)
        .subscribe((res) => {
          this.data = <any[]>res;
          this.pages = Math.ceil(this.data.length / this.objectsPerPage);
        });
    } else {
      this.db.fetchAllByType(this.dataType).subscribe((res) => {
        this.data = <any[]>res;
        this.pages = Math.ceil(this.data.length / this.objectsPerPage);
      });
    }
  }

  ngAfterViewInit() {
    this.config.interval = -1;
    this.config.showNavigationIndicators = false;
  }

  enoughSpace(id: number, pageId: number) {
    return id < pageId * this.objectsPerPage + this.objectsPerPage;
  }

  sufficient(id: number, pageId: number) {
    return id >= pageId * this.objectsPerPage && id < pageId * this.objectsPerPage + this.objectsPerPage;
  }
}
