import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ProductService} from '../../services/product.service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
    constructor(private productService: ProductService, private cdr: ChangeDetectorRef) {
    }

    products: any[] = [];
    limitProduct: string = '10';

    ngOnInit() {
        this.getLimitProducts();
    }

    getLimitProducts() {
        let allProducts = 0;
        this.productService.getCountAllProducts(0).subscribe({
            next: (res) => {
                allProducts = Number.parseInt(res.data);
                console.log("All products: ", allProducts);
                let totalPage = Math.floor(allProducts / Number.parseInt(this.limitProduct));
                let page = Math.floor(Math.random() * totalPage).toString();
                console.log("Page: ", page);
                console.log("Total page: ", totalPage);
                let categoryId = '0';
                this.productService.getProducts(page, this.limitProduct, categoryId).subscribe({
                    next: (res) => {
                        console.log("Get product ok!");
                        this.products = res.data.content;
                        this.cdr.detectChanges();//check again thay doi
                    },
                })
            },
            error: (err) => {
                console.log(err);
            },
        });


    }
}
