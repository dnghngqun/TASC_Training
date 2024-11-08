import { Component, HostListener, OnInit } from '@angular/core';
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
  categoryId: number = 0;
  constructor(private productService: ProductService) {}
  categories: any[] = [];
  ngOnInit(): void {
    this.getCountAllProducts();
    this.loadProducts();
    this.getCategories();
  }

  getCountAllProducts() {
    this.productService.getCountAllProducts(this.categoryId).subscribe({
      next: (response) => {
        console.log('Total products: ', response);
        this.totalProducts = Number.parseInt(response.data);
        console.log('Total products: ', this.totalProducts);
      },
      error: (error) => {
        console.error('error: ', error);
      },
    });
  }

  loadProducts() {
    this.productService
      .getProducts(
        this.currentPage.toString(),
        this.productPerPage.toString(),
        this.categoryId.toString(),
      )
      .subscribe({
        next: (response) => {
          console.log('response: ', response);
          this.products = response.data.content;
          console.log('products: ', this.products);
        },
        error: (error) => {
          console.error('error: ', error);
        },
      });
  }
  onPageChange(page: number) {
    console.log('Page: ', page);
    this.currentPage = page - 1;
    this.loadProducts();
    this.scrollToTarget();
  }

  onClickCategory(categoryId: number) {
    console.log('Category ID: ', categoryId);
    this.categoryId = categoryId;
    this.currentPage = 0;
    this.loadProducts();
    this.scrollToTarget();
  }

  // Hàm đóng menu khi click ra ngoài
  @HostListener('document:click', ['$event'])
  onOutsideClick(event: Event): void {
    // Kiểm tra nếu click vào một phần tử ngoài menu thì đóng menu
    const clickedInside =
      (event.target as HTMLElement).closest('.danhmuc-list') !== null;
    if (!clickedInside) {
      this.isShowDanhmuc = false;
    }
  }

  onClickBtnDanhmuc(event: Event): void {
    event.stopPropagation();
    this.isShowDanhmuc = !this.isShowDanhmuc;
  }
  getCategories() {
    this.productService.getCategories().subscribe({
      next: (res) => {
        console.log('Categories: ', res);
        this.categories = res.data;
      },
      error: (error) => {
        console.error('error to fetch category: ', error);
      },
    });
  }
  scrollToTarget(): void {
    const targetElement = document.getElementById('pagination-target');
    if (targetElement) {
      targetElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
