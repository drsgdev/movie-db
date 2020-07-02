import { Component, OnInit } from '@angular/core';
import { DatabaseService } from '../database.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-reviews-list',
  templateUrl: './reviews-list.component.html',
  styleUrls: ['./reviews-list.component.scss'],
  inputs: ['id'],
})
export class ReviewsListComponent implements OnInit {
  id: number;
  reviews = [];

  review = {
    rate : 0,
    title : '',
    describpiton : '',
    date : new Date(),
    user_id : 0,
    object_id : 0,
  }

  constructor(private db: DatabaseService, private modalService: NgbModal) {}

  ngOnInit(): void {
    this.reviews = this.db.reviews;
  }

  openReviewModal(modal) {
    this.modalService.open(modal, { centered: true });
  }

  formSubmit() {
    this.review.date = new Date();
    this.review.user_id = 1;
    this.review.object_id = this.id;

    this.db.review(this.review);
  }
}
