import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RateThisComponent } from './rate-this.component';

describe('RateThisComponent', () => {
  let component: RateThisComponent;
  let fixture: ComponentFixture<RateThisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RateThisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RateThisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
