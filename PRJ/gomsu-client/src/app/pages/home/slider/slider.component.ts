import { Component, OnInit } from '@angular/core';
import Swiper from 'swiper';
import { Autoplay, EffectFade, Navigation, Pagination } from 'swiper/modules';

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.css'],
})
export class SliderComponent implements OnInit {
  swiper!: Swiper;

  constructor() {}
  ngOnInit() {
    this.swiper = new Swiper('.swiper-container', {
      effect: 'fade',
      slidesPerView: 1,
      autoplay: {
        delay: 2400,
        disableOnInteraction: false,
      },
      pagination: { clickable: false },
      loop: true,
      modules: [ Navigation, EffectFade, Autoplay],
    });
  }
}
