import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { DatabaseService } from '../database.service';
import { NgbCarousel } from '@ng-bootstrap/ng-bootstrap';

// TODO: fix this component!
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
  isActive = true;

  objectsPerPage = 3;
  pages = 1;

  @ViewChild('carousel') carousel: NgbCarousel;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.db.fetchByType(this.dataType).subscribe(
      (res) => {
        this.data = res as any[];
      },
      (err) => console.log(err)
    );
    console.log(this.data);

    this.pages = Math.ceil(this.data.length / this.objectsPerPage);
  }

  ngAfterViewInit() {
    this.carousel.activeId = '1';
    this.carousel.pause();
    this.carousel.showNavigationIndicators = false;
  }
}
