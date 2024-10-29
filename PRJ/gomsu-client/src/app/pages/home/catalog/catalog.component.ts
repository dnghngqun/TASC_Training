import { Component, OnInit } from '@angular/core';
import Swiper from 'swiper';
import { Autoplay, Navigation } from 'swiper/modules';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css'],
})
export class CatalogComponent implements OnInit {
  swiper!: Swiper;
  constructor() {}

  ngOnInit() {
    this.swiper = new Swiper('.swiper-container-catalog', {
      slidesPerView: 6,
      autoplay:{delay:2000, disableOnInteraction: false},
      navigation: {
        nextEl: '.swiper-catalog-btn-next',
        prevEl: '.swiper-catalog-btn-prev',
      },
      modules: [Navigation, Autoplay],
    });
  }
}
