import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaplinkDashboard } from './taplink-dashboard';

describe('TaplinkDashboard', () => {
  let component: TaplinkDashboard;
  let fixture: ComponentFixture<TaplinkDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaplinkDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaplinkDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
