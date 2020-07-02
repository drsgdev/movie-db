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
  pages = 1;

  @ViewChild('carousel', {static : true}) carousel: NgbCarousel;

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    // this.data = this.service.fetchNewByType(this.dataType);

    /** 
     * for testing purposes only
     * TODO: *to be deleted*
     */
    switch (this.dataType) {
      case 'cast':
        this.data = this.db.cast;
        break;
      default:
        this.data = this.db.data;
        break;
    }

    this.pages = Math.ceil(this.data.length / this.objectsPerPage);

    console.log('pages: ' + this.pages + ' obj/page: ' + this.objectsPerPage);
    
  }
}
