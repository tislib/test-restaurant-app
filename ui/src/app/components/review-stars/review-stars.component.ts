import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {faStar} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-review-stars',
  templateUrl: './review-stars.component.html',
  styleUrls: ['./review-stars.component.scss']
})
export class ReviewStarsComponent implements OnInit {

  public faStar = faStar

  @Input()
  public showOnly = false;

  @Input()
  public starCount = 0;

  @Output()
  public starCountChange: EventEmitter<number> = new EventEmitter<number>();

  public stars = [1, 2, 3, 4, 5];

  constructor() {
  }

  ngOnInit(): void {
  }

  changeStarCount(star: number) {
    this.starCount = star;
    this.starCountChange.emit(star)
  }
}
