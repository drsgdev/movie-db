import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { ActivatedRoute } from '@angular/router';
import { Content } from '@angular/compiler/src/render3/r3_ast';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-content-page',
  templateUrl: './content-page.component.html',
  styleUrls: ['./content-page.component.scss'],
  inputs: ['id'],
})
export class ContentPageComponent implements OnInit {
  id = 0;
  type: string;
  content: any;
  isFavorite: boolean;

  constructor(
    private db: DatabaseService,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });
    this.route.url.subscribe((url) => {
      this.type = url[0].path;
    });

    this.checkIfFavorite();
    this.db.addVisited(this.id);

    this.db.fetchById(this.id).subscribe((res) => (this.content = res));
  }

  floor(n: number) {
    return Math.floor(n);
  }

  isLoggedIn() {
    return this.db.isLoggedIn();
  }

  favorite() {
    this.db.favorite(this.id).subscribe(
      (res) => this.toastr.success('This title was added to your favorites'),
      (err) => this.toastr.error('Failed to add this title to your favorites')
    );

    this.isFavorite = true;
  }

  removeFromFavorite() {
    this.db.removeFromFavorite(this.id).subscribe(
      (res) =>
        this.toastr.success('This title was removed from your favorites'),
      (err) =>
        this.toastr.error('Failed to remove this title from your favorites')
    );

    this.isFavorite = false;
  }

  checkIfFavorite() {
    this.db.isFavorite(this.id).subscribe(
      (res) => (this.isFavorite = true),
      (err) => (this.isFavorite = false)
    );
  }
}
