import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DatabaseService {
  response: {};

  constructor(private http: HttpClient) {}

  fetchByType(type: string) {
    return this.http.get(environment.apiUrl + '/' + type + '/all');
  }

  fetchNewByType(type: string) {
    this.http.get(environment.apiUrl + '/' + type + '/new').subscribe((res) => {
      this.response = res;
      console.log(res);
    });

    return this.response;
  }

  fetchById(id: number) {
    return this.http.get(environment.apiUrl + '/find?id=' + id);
  }

  fetchRecent() {
    this.http.get('localhost:8081/recent').subscribe((res) => {
      this.response = res;
      console.log(res);
    });

    return this.response;
  }

  rate(id: number, rate: number) {
    this.http.post('localhost:8081/rate', {
      params: {
        id: id,
        rate: rate,
      },
    });
  }

  review(review : any) {

  }
}
