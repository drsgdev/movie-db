import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowsPageComponent } from './shows-page.component';

describe('ShowsPageComponent', () => {
  let component: ShowsPageComponent;
  let fixture: ComponentFixture<ShowsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
