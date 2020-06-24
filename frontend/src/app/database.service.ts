import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class DatabaseService {
  // for testing purposes *to be deleted*
  data = [
    {
      id: 1,
      title: 'Test Movie',
      description: 'Test movie description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 2,
      title: 'Another Test Movie',
      description: 'Another Test Movie description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 3,
      title: 'Test Movie 3',
      description: 'Test Movie 3 description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 4,
      title: 'Test Movie 4',
      description: 'Test Movie 4 description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
  ];

  cast = [
    {
      id: 1,
      name: 'Cast Member 1',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 2,
      name: 'Cast Member 2',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 3,
      name: 'Cast Member 3',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 4,
      name: 'Cast Member 4',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 5,
      name: 'Cast Member 5',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 6,
      name: 'Cast Member 6',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: 7,
      name: 'Cast Member 7',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
  ];

  response: any;

  constructor(private http: HttpClient) {}

  getData() {
    return this.data;
  }

  getById(id: number) {
    return this.data[id];
  }

  fetchByType(type: string) {
    this.http.get('localhost:8081/' + type + '/all').subscribe((res) => {
      this.response = res;
      console.log(res);
    });

    return this.response;
  }

  fetchNewByType(type: string) {
    this.http.get('localhost:8081/' + type + '/new').subscribe((res) => {
      this.response = res;
      console.log(res);
    });

    return this.response;
  }

  fetchById(id: number) {
    this.http.get('localhost:8081/find?id=' + id).subscribe((res) => {
      this.response = res;
      console.log(res);
    });

    return this.response;
  }

  fetchRecent() {
    this.http.get('localhost:8081/recent').subscribe((res) => {
      this.response = res;
      console.log(res);
    });

    return this.response;
  }
}
