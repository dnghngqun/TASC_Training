import { Component, Input, OnInit } from '@angular/core';
import {
  faChevronLeft,
  faChevronRight,
} from '@fortawesome/free-solid-svg-icons';
import Swiper from 'swiper';
import { Autoplay, Navigation } from 'swiper/modules';
@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css'],
})
export class BreadcrumbComponent implements OnInit {
  @Input() heading!: string;
  @Input() location!: string;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  constructor() {}
  swiper!: Swiper;
  ngOnInit() {
    this.swiper = new Swiper('.swiper-container-breadcrumb', {
      slidesPerView: 4,
      autoplay: { delay: 2400, disableOnInteraction: false },
      spaceBetween: 20,
      navigation: {
        nextEl: '.swiper-catalog-btn-next',
        prevEl: '.swiper-catalog-btn-prev',
      },
      modules: [Navigation, Autoplay],
    });
  }
}
