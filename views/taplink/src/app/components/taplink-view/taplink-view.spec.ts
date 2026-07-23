import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaplinkView } from './taplink-view';

describe('TaplinkView', () => {
  let component: TaplinkView;
  let fixture: ComponentFixture<TaplinkView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaplinkView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaplinkView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
