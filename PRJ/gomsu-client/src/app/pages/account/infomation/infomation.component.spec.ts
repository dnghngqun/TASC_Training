import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfomationComponent } from './infomation.component';

describe('InfomationComponent', () => {
  let component: InfomationComponent;
  let fixture: ComponentFixture<InfomationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InfomationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InfomationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
