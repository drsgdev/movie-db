import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatabaseService } from '../database.service';

@Component({
  selector: 'app-person-page',
  templateUrl: './person-page.component.html',
  styleUrls: ['./person-page.component.scss'],
})
export class PersonPageComponent implements OnInit {
  id: number;
  person: any;

  constructor(
    private db: DatabaseService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.db.fetchPersonById(this.id).subscribe(
      (res) => {
        this.person = res;
      },
      (err) => {
        this.router.navigateByUrl('404');
      }
    );
  }
}
