import { Component, OnInit, PipeTransform } from '@angular/core';
import { DatabaseService } from '../database.service';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-content-list',
  templateUrl: './content-list.component.html',
  styleUrls: ['./content-list.component.scss'],
  inputs: ['type'],
})
export class ContentListComponent implements OnInit {
  type: string;
  page: number = 1;
  pageSize: number = 5;
  items: any[];
  itemsToOutput: Observable<any[]>;

  filter = new FormControl('');

  constructor(private db: DatabaseService) {}

  ngOnInit(): void {
    this.db.fetchAllByType(this.type).subscribe(
      (res) => (this.items = <any[]>res),
      (err) => {}
    );

    this.itemsToOutput = this.filter.valueChanges.pipe(
      startWith(''),
      map((text) => this.search(text))
    );
  }

  search(text: string): any[] {
    return this.items.filter((item) => {
      let term = text.toLowerCase();
      switch (this.type) {
        case 'movie':
          return (
            item.title.toLowerCase().includes(term) ||
            item.budget.includes(term)
          );
        case 'show':
          return item.name.toLowerCase().includes(term);
        case 'person':
          return (
            item.name.toLowerCase().includes(term) ||
            item.known_for_department.toLowerCase().includes(term)
          );
      }
    });
  }
}
