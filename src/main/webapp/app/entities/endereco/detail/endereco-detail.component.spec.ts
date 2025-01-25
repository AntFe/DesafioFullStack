import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnderecoDetailComponent } from './endereco-detail.component';

describe('Endereco Management Detail Component', () => {
  let comp: EnderecoDetailComponent;
  let fixture: ComponentFixture<EnderecoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnderecoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./endereco-detail.component').then(m => m.EnderecoDetailComponent),
              resolve: { endereco: () => of({ id: 42 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EnderecoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnderecoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load endereco on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EnderecoDetailComponent);

      // THEN
      expect(instance.endereco()).toEqual(expect.objectContaining({ id: 42 }));
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
