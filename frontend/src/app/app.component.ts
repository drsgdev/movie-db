import { Component } from '@angular/core';
import { DatabaseService } from './database.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DatabaseService],
})
export class AppComponent {
  title = 'frontend';
}
