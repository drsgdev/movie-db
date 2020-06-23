import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.scss']
})
export class PageHeaderComponent implements OnInit {

  loggedIn : boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  login() : void {
    this.loggedIn = true;
  }

  logout() : void {
    this.loggedIn = false;
  }
}
