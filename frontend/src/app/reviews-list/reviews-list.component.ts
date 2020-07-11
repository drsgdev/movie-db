import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Review } from '../review';
import { map, catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reviews-list',
  templateUrl: './reviews-list.component.html',
  styleUrls: ['./reviews-list.component.scss'],
  inputs: ['id'],
})
export class ReviewsListComponent implements OnInit {
  id: number;
  reviews: any[];

  review = new Review();

  constructor(
    private db: DatabaseService,
    private modalService: NgbModal,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.updateReviewList();
  }

  openReviewModal(modal) {
    this.modalService.open(modal, { centered: true });
  }

  formSubmit() {
    this.review.date = Date.now().toString();
    this.review.id = this.id;

    this.db.review(this.review).subscribe(
      (res) => this.toastr.success(res),
      (err) => this.toastr.error('Failed to save your review'),
      () => this.updateReviewList()
    );

    this.updateReviewList();
  }

  loggedIn() {
    return this.db.isLoggedIn();
  }

  updateReviewList() {
    this.db.getReviews(this.id).subscribe(
      (res) => (this.reviews = res),
      () => {}
    );
  }
}
