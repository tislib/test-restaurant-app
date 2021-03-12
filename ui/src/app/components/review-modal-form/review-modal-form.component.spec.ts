import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewModalFormComponent } from './review-modal-form.component';

describe('AddReviewComponent', () => {
  let component: ReviewModalFormComponent;
  let fixture: ComponentFixture<ReviewModalFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewModalFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
