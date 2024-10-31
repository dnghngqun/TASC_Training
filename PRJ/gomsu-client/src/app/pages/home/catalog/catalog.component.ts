import { Component, OnInit } from '@angular/core';
import Swiper from 'swiper';
import { Autoplay } from 'swiper/modules';

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
      slidesPerView: 'auto',
      autoplay: { delay: 2400, disableOnInteraction: false },
      spaceBetween: 22,
      loop: true,
      modules: [Autoplay],
    });
  }
}
