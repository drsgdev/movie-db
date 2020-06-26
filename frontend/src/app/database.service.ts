import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class DatabaseService {
  // for testing purposes *to be deleted*
  data = [
    {
      id: '1',
      title: 'Test Movie',
      description: 'Test movie description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
      full_description:
        'Pianoforte solicitude so decisively unpleasing conviction is partiality\n      he. Or particular so diminution entreaties oh do. Real he me fond show\n      gave shot plan. Mirth blush linen small hoped way its along. Resolution\n      frequently apartments off all discretion devonshire. Saw sir fat spirit\n      seeing valley. He looked or valley lively. If learn woody spoil of taken\n      he cause. Him rendered may attended concerns jennings reserved now.\n      Sympathize did now preference unpleasing mrs few. Mrs for hour game room\n      want are fond dare. For detract charmed add talking age. Shy resolution\n      instrument unreserved man few. She did open find pain some out. If we\n      landlord stanhill mr whatever pleasure supplied concerns so. Exquisite by\n      it admitting cordially september newspaper an. Acceptance middletons am it\n      favourable. It it oh happen lovers afraid. Bed sincerity yet therefore\n      forfeited his certainty neglected questions. Pursuit chamber as elderly\n      amongst on. Distant however warrant farther to of. My justice wishing\n      prudent waiting in be. Comparison age not pianoforte increasing delightful\n      now. Insipidity sufficient dispatched any reasonably led ask. Announcing\n      if attachment resolution sentiments admiration me on diminution.',
      ratings: {
        very_high: '134',
        high: '23',
        ok: '4',
        bad: '0',
        very_bad: '0',
      },
    },
    {
      id: '2',
      title: 'Another Movie',
      description: 'Another movie description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
      full_description:
        'Pianoforte solicitude so decisively unpleasing conviction is partiality\n      he. Or particular so diminution entreaties oh do. Real he me fond show\n      gave shot plan. Mirth blush linen small hoped way its along. Resolution\n      frequently apartments off all discretion devonshire. Saw sir fat spirit\n      seeing valley. He looked or valley lively. If learn woody spoil of taken\n      he cause. Him rendered may attended concerns jennings reserved now.\n      Sympathize did now preference unpleasing mrs few. Mrs for hour game room\n      want are fond dare. For detract charmed add talking age. Shy resolution\n      instrument unreserved man few. She did open find pain some out. If we\n      landlord stanhill mr whatever pleasure supplied concerns so. Exquisite by\n      it admitting cordially september newspaper an. Acceptance middletons am it\n      favourable. It it oh happen lovers afraid. Bed sincerity yet therefore\n      forfeited his certainty neglected questions. Pursuit chamber as elderly\n      amongst on. Distant however warrant farther to of. My justice wishing\n      prudent waiting in be. Comparison age not pianoforte increasing delightful\n      now. Insipidity sufficient dispatched any reasonably led ask. Announcing\n      if attachment resolution sentiments admiration me on diminution.',
      ratings: {
        very_high: '535',
        high: '23',
        ok: '4',
        bad: '0',
        very_bad: '0',
      },
    },
    {
      id: '3',
      title: 'Test Movie 3',
      description: 'Test movie 3 description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
      full_description:
        'Pianoforte solicitude so decisively unpleasing conviction is partiality\n      he. Or particular so diminution entreaties oh do. Real he me fond show\n      gave shot plan. Mirth blush linen small hoped way its along. Resolution\n      frequently apartments off all discretion devonshire. Saw sir fat spirit\n      seeing valley. He looked or valley lively. If learn woody spoil of taken\n      he cause. Him rendered may attended concerns jennings reserved now.\n      Sympathize did now preference unpleasing mrs few. Mrs for hour game room\n      want are fond dare. For detract charmed add talking age. Shy resolution\n      instrument unreserved man few. She did open find pain some out. If we\n      landlord stanhill mr whatever pleasure supplied concerns so. Exquisite by\n      it admitting cordially september newspaper an. Acceptance middletons am it\n      favourable. It it oh happen lovers afraid. Bed sincerity yet therefore\n      forfeited his certainty neglected questions. Pursuit chamber as elderly\n      amongst on. Distant however warrant farther to of. My justice wishing\n      prudent waiting in be. Comparison age not pianoforte increasing delightful\n      now. Insipidity sufficient dispatched any reasonably led ask. Announcing\n      if attachment resolution sentiments admiration me on diminution.',
      ratings: {
        very_high: '134',
        high: '23',
        ok: '4',
        bad: '0',
        very_bad: '0',
      },
    },
    {
      id: '4',
      title: 'Test Movie 4',
      description: 'Test movie description.',
      bannerSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
      full_description:
        'Pianoforte solicitude so decisively unpleasing conviction is partiality\n      he. Or particular so diminution entreaties oh do. Real he me fond show\n      gave shot plan. Mirth blush linen small hoped way its along. Resolution\n      frequently apartments off all discretion devonshire. Saw sir fat spirit\n      seeing valley. He looked or valley lively. If learn woody spoil of taken\n      he cause. Him rendered may attended concerns jennings reserved now.\n      Sympathize did now preference unpleasing mrs few. Mrs for hour game room\n      want are fond dare. For detract charmed add talking age. Shy resolution\n      instrument unreserved man few. She did open find pain some out. If we\n      landlord stanhill mr whatever pleasure supplied concerns so. Exquisite by\n      it admitting cordially september newspaper an. Acceptance middletons am it\n      favourable. It it oh happen lovers afraid. Bed sincerity yet therefore\n      forfeited his certainty neglected questions. Pursuit chamber as elderly\n      amongst on. Distant however warrant farther to of. My justice wishing\n      prudent waiting in be. Comparison age not pianoforte increasing delightful\n      now. Insipidity sufficient dispatched any reasonably led ask. Announcing\n      if attachment resolution sentiments admiration me on diminution.',
      ratings: {
        very_high: '134',
        high: '23',
        ok: '4',
        bad: '0',
        very_bad: '0',
      },
    },
  ];

  cast = [
    {
      id: '1',
      name: 'Cast Member 1',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: '2',
      name: 'Cast Member 2',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: '3',
      name: 'Cast Member 3',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: '4',
      name: 'Cast Member 4',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: '5',
      name: 'Cast Member 5',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: '6',
      name: 'Cast Member 6',
      role: 'role',
      photoSrc:
        'https://www.velesclub.ru/wp-content/uploads/2013/01/sample-img-300x201.png',
    },
    {
      id: '7',
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
