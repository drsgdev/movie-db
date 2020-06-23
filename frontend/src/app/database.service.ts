import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  response : any;

  constructor(private http : HttpClient) { }

  fetchByType(type : string) {
    this.http.get('localhost:8081/' + type + '/all').subscribe(
      (res) => {
        this.response = res;
        console.log(res);
      }
    );

    return this.response;
  }

  fetchNewByType(type: string) {
    this.http.get('localhost:8081/' + type + '/new').subscribe(
      (res) => {
        this.response = res;
        console.log(res);
      }
    );

    return this.response;
  }

  fetchById(id : number) {
    this.http.get('localhost:8081/find?id=' + id).subscribe(
      (res) => {
        this.response = res;
        console.log(res);
      }
    );

    return this.response;
  }

  fetchRecent() {
    this.http.get('localhost:8081/recent').subscribe(
      (res) => {
        this.response = res;
        console.log(res);
      }
    );

    return this.response;
  }
}
