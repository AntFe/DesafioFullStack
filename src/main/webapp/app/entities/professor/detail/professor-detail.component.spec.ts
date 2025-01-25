import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProfessorDetailComponent } from './professor-detail.component';

describe('Professor Management Detail Component', () => {
  let comp: ProfessorDetailComponent;
  let fixture: ComponentFixture<ProfessorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./professor-detail.component').then(m => m.ProfessorDetailComponent),
              resolve: { professor: () => of({ id: 2234 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProfessorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfessorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load professor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProfessorDetailComponent);

      // THEN
      expect(instance.professor()).toEqual(expect.objectContaining({ id: 2234 }));
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
