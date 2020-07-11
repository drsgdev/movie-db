import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { DatabaseService } from '../database.service';
import { ToastrService } from 'ngx-toastr';
import { map, tap } from 'rxjs/operators';

@Component({
  selector: 'app-rate-this',
  templateUrl: './rate-this.component.html',
  styleUrls: ['./rate-this.component.scss'],
  inputs: ['id'],
})
export class RateThisComponent implements OnInit {
  id: number;
  current_rate: number;

  @Output()
  onRateChange = new EventEmitter();

  constructor(private db: DatabaseService, private toastr: ToastrService) {}

  ngOnInit(): void {}

  rate() {
    if (this.current_rate > 0) {
      this.db.rate(this.id, this.current_rate).subscribe(
        (res) => {
          this.toastr.success(<string>res);
        },
        (err) => {
          this.toastr.error('Failed to save your rating');
        },
        () => {
          this.onRateChange.emit();
        }
      );
    }
  }

  loggedIn() {
    return this.db.isLoggedIn();
  }
}
