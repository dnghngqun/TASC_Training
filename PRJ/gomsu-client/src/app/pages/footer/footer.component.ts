import { Component, OnInit } from '@angular/core';
import { faLocationDot,faEnvelope, faPhone } from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent implements OnInit {
  constructor() {}
  faLocationDot = faLocationDot;
  faPhone = faPhone;
  faEnvelope=faEnvelope;
  ngOnInit() {}
}
