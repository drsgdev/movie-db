import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DatabaseService {
  constructor(private http: HttpClient) {}

  fetchAllByType(type: string) {
    return this.http.get(environment.apiUrl + '/' + type + '/all');
  }

  fetchById(id: number) {
    return this.http.get(environment.apiUrl + '/find?id=' + id);
  }

  fetchCreditsById(id: number, type: string, creditType: string) {
    return this.http.get(environment.apiUrl + "/" + type + "/credits/" + creditType + "?id=" + id);
  }
}
