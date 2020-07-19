import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DatabaseService } from '../database.service';
import { Review } from '../review';

@Component({
  selector: 'app-reviews-list',
  templateUrl: './reviews-list.component.html',
  styleUrls: ['./reviews-list.component.scss'],
  inputs: ['id'],
})
export class ReviewsListComponent implements OnInit {
  id: number;
  reviews: any[];

  loginSubscription: Subscription;
  isLoggedIn: boolean;

  isAdmin: boolean;
  roleSubscription: Subscription;

  review = new Review();

  constructor(
    private db: DatabaseService,
    private modalService: NgbModal,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loginSubscription = this.db.isLoggedIn$.subscribe(
      (res) => (this.isLoggedIn = res)
    );
    this.roleSubscription = this.db.isAdmin$.subscribe(
      (res) => (this.isAdmin = res)
    );

    this.db.updateLoginState();
    this.db.updateRole();
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
  }

  updateReviewList() {
    this.db.getReviews(this.id).subscribe(
      (res) => (this.reviews = res),
      (err) => (this.reviews = null)
    );
  }

  deleteReview(review: any) {
    this.db.deleteReview(review).subscribe(
      (res) => this.toastr.success(res),
      (err) => this.toastr.error('Failed to delete review'),
      () => this.updateReviewList()
    );
  }
}
