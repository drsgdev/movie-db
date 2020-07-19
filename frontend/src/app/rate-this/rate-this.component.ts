import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-rate-this',
  templateUrl: './rate-this.component.html',
  styleUrls: ['./rate-this.component.scss'],
  inputs: ['id'],
})
export class RateThisComponent implements OnInit {
  id: number;
  current_rate: number;

  loginSubscription: Subscription;
  isLoggedIn: boolean;

  @Output()
  onRateChange = new EventEmitter();

  constructor(private db: DatabaseService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loginSubscription = this.db.isLoggedIn$.subscribe(
      (res) => (this.isLoggedIn = res)
    );

    this.db.updateLoginState();
  }

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
}
