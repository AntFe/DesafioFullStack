import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LocalDetailComponent } from './local-detail.component';

describe('Local Management Detail Component', () => {
  let comp: LocalDetailComponent;
  let fixture: ComponentFixture<LocalDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocalDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./local-detail.component').then(m => m.LocalDetailComponent),
              resolve: { local: () => of({ id: 3747 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LocalDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load local on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LocalDetailComponent);

      // THEN
      expect(instance.local()).toEqual(expect.objectContaining({ id: 3747 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
