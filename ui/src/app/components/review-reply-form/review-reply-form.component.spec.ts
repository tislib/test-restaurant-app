import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewReplyFormComponent } from './review-reply-form.component';

describe('ReviewReplyFormComponent', () => {
  let component: ReviewReplyFormComponent;
  let fixture: ComponentFixture<ReviewReplyFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewReplyFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewReplyFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
