/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { Page401Component } from './page401.component';

describe('Page401Component', () => {
  let component: Page401Component;
  let fixture: ComponentFixture<Page401Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Page401Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Page401Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
