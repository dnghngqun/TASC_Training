import { Component, OnInit } from '@angular/core';
import { faCaretDown,faHeart,faBagShopping,faMagnifyingGlass,faUser } from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }
  faCaretDown = faCaretDown;
  faMagnifyingGlass=faMagnifyingGlass;
  faUser=faUser;
  faHeart=faHeart;
  faBagShopping=faBagShopping;

}
