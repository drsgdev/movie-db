import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitedListComponent } from './visited-list.component';

describe('VisitedListComponent', () => {
  let component: VisitedListComponent;
  let fixture: ComponentFixture<VisitedListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisitedListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
