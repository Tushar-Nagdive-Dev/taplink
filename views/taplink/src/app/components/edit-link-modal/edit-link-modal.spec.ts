import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditLinkModal } from './edit-link-modal';

describe('EditLinkModal', () => {
  let component: EditLinkModal;
  let fixture: ComponentFixture<EditLinkModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditLinkModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditLinkModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
