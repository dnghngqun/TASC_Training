import { Component, OnInit } from '@angular/core';
import {
  faCartShopping,
  faHeart,
  faMagnifyingGlass,
} from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  faCartShopping = faCartShopping;
  faHeart = faHeart;
  faMagnifyingGlass = faMagnifyingGlass;

  constructor() {}

  ngOnInit() {}
}
