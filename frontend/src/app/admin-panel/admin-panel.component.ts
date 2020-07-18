import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { identifierModuleUrl } from '@angular/compiler';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent implements OnInit {
  page: number = 1;
  pageSize: number = 5;
  items: any[];
  itemsToOutput: Observable<any[]>;

  filter = new FormControl('');

  constructor(private db: DatabaseService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.updateInfo();
    this.setOutput();
  }

  setOutput() {
    this.itemsToOutput = this.filter.valueChanges.pipe(
      startWith(''),
      map((text) => this.search(text))
    );
  }

  search(text: string): any[] {
    return this.items.filter((item) => {
      let term = text.toLowerCase();
      return (
        item.username.toLowerCase().includes(term) ||
        item.email.toLowerCase().includes(term)
      );
    });
  }

  ban(username: string) {
    this.db.ban(username).subscribe(
      (res) => this.toastr.success(res),
      (err) => this.toastr.error(err.error),
      () => this.ngOnInit()
    );
  }

  unban(username: string) {
    this.db.unban(username).subscribe(
      (res) => this.toastr.success(res),
      (err) => this.toastr.error(err.error),
      () => this.ngOnInit()
    );
  }

  updateInfo() {
    this.items = null;
    this.db.fetchAllByType('user').subscribe(
      (res) => {
        this.items = <any[]>res;
      },
      (err) => {}
    );
  }
}
