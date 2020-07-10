import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrls: ['./signup-page.component.scss'],
})
export class SignupPageComponent implements OnInit {
  payload = {
    username: '',
    email: '',
    password: '',
  };

  constructor(
    private db: DatabaseService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  signup() {
    this.db.signup(this.payload).subscribe(
      () => {
        this.router.navigate(['/login'], {
          queryParams: { registered: 'true' },
        });
      },
      (err) => {
        this.toastr.error('Registration failed: ' + err.error);
      });
  }
}
