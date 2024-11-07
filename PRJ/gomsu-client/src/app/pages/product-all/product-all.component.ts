import { Component, OnInit } from '@angular/core';
import {
  faCartShopping,
  faHeart,
  faMagnifyingGlass,
} from '@fortawesome/free-solid-svg-icons';
import { ProductService } from '../../services/product.service';
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

  totalProducts: number = 0;
  productPerPage: number = 12;
  currentPage: number = 0;
  products: any[] = [];
  constructor(private productService: ProductService) {}

  ngOnInit():void{
    this.getCountAllProducts();
    this.loadProducts();
  }

  getCountAllProducts(){
    this.productService.getCountAllProducts().subscribe({
      next: (response) => {
        console.log("Total products: ", response);
      },
      error: (error) => {
        console.error('error: ', error);
      },
    });
  }

  loadProducts() {
    this.productService
      .getProducts(this.currentPage.toString(), this.productPerPage.toString())
      .subscribe({
        next: (response) => {
          console.log('response: ', response);
        },
        error: (error) => {
          console.error('error: ', error);
        },
      });
  }
  onPageChange(page: number) {
    this.currentPage = page;
    this.loadProducts();
  }
}
