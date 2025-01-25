import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaisDetailComponent } from './pais-detail.component';

describe('Pais Management Detail Component', () => {
  let comp: PaisDetailComponent;
  let fixture: ComponentFixture<PaisDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaisDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./pais-detail.component').then(m => m.PaisDetailComponent),
              resolve: { pais: () => of({ id: 17868 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaisDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaisDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pais on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaisDetailComponent);

      // THEN
      expect(instance.pais()).toEqual(expect.objectContaining({ id: 17868 }));
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
