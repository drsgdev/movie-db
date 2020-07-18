import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss'],
})
export class LoginPageComponent implements OnInit {
  payload = {
    username: '',
    password: '',
  };

  constructor(
    private db: DatabaseService,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params.registered === 'true') {
        this.toastr.success(
          'Signup successful. Please check your inbox for activation link.'
        );
      }
    });
  }

  login() {
    this.db.login(this.payload).subscribe(
      (res) => {
        this.router.navigateByUrl('/');
        this.toastr.success('Login successful');
      },
      (err) => {
        this.toastr.error('Failed to log in: ' + err.error.message);
      }
    );
  }
}
