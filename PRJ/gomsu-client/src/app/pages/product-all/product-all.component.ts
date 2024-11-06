import { Component, OnInit } from '@angular/core';
import {
  faCartShopping,
  faHeart,
  faPlus,
  faMagnifyingGlass,
} from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-product-all',
  templateUrl: './product-all.component.html',
  styleUrls: ['./product-all.component.css'],
})
export class ProductAllComponent implements OnInit {
  faCartShopping = faCartShopping;
  faHeart = faHeart;
  faMagnifyingGlass = faMagnifyingGlass;

  isShowDanhmuc: boolean = false;

  constructor() {}

  ngOnInit() {}
}
