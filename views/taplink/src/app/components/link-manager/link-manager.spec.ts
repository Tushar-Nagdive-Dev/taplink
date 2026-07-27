import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkManager } from './link-manager';

describe('LinkManager', () => {
  let component: LinkManager;
  let fixture: ComponentFixture<LinkManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LinkManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
