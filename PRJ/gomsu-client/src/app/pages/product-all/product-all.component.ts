import { Component, OnInit } from '@angular/core';
import {
  faCartShopping,
  faHeart,
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

  constructor() {}

  ngOnInit() {}
}
