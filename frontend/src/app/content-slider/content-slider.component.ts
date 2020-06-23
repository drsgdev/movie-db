import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
/*  */
@Component({
  selector: 'app-content-slider',
  templateUrl: './content-slider.component.html',
  styleUrls: ['./content-slider.component.scss'],
  inputs: ['name'],
})
export class ContentSliderComponent implements OnInit {
  name = '';
  dataType = 'movie';
  data: any[];

  objectsPerPage = 3;
  pages = [];
  currentPage = 1;

  constructor(private service: DatabaseService) {}

  ngOnInit(): void {
    // this.data = this.service.fetchNewByType(this.dataType);

    // initialization is for testing purposes
    this.data = [
      {
        id: 1,
        title: 'Test Movie',
        description: 'Test Movie description.',
        bannerSrc:
          'https://m.media-amazon.com/images/M/MV5BMTRhN2VkYzgtNGUwOC00MmVhLTkxNzYtYmFjZDg0YzAzMWU0XkEyXkFqcGdeQXVyMTQ3ODkwMjc@._V1_UX182_CR0,0,182,268_AL_.jpg',
      },
      {
        id: 2,
        title: 'Another Test Movie',
        description: 'Another Test Movie description.',
        bannerSrc:
          'https://m.media-amazon.com/images/M/MV5BNTljNmRjNDctNjIzMS00ODU2LTk4MjUtYzJlOTQwMmI2ODQyXkEyXkFqcGdeQXVyNDU0NjMyNTQ@._V1_UX182_CR0,0,182,268_AL_.jpg',
      },
      {
        id: 3,
        title: 'Test Movie 3',
        description: 'Test Movie 3 description.',
        bannerSrc:
          'https://m.media-amazon.com/images/M/MV5BOGY1MGM2ZjItZDJjMC00ZGM0LTg2MDctNmExNzcyYTcwMjM3XkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_UX182_CR0,0,182,268_AL_.jpg',
      },
      {
        id: 4,
        title: 'Test Movie 4',
        description: 'Test Movie 4 description.',
        bannerSrc:
          'https://m.media-amazon.com/images/M/MV5BN2M5MDA3NmUtM2Y2YS00NmU3LWJiMjEtZWY2ZDE4MGY1OWZjXkEyXkFqcGdeQXVyMTA2MDU0NjM5._V1_UX182_CR0,0,182,268_AL_.jpg',
      },
    ];

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
