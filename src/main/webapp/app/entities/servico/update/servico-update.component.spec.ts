import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ServicoService } from '../service/servico.service';
import { IServico } from '../servico.model';
import { ServicoFormService } from './servico-form.service';

import { ServicoUpdateComponent } from './servico-update.component';

describe('Servico Management Update Component', () => {
  let comp: ServicoUpdateComponent;
  let fixture: ComponentFixture<ServicoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let servicoFormService: ServicoFormService;
  let servicoService: ServicoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ServicoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ServicoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServicoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicoFormService = TestBed.inject(ServicoFormService);
    servicoService = TestBed.inject(ServicoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const servico: IServico = { id: 14176 };

      activatedRoute.data = of({ servico });
      comp.ngOnInit();

      expect(comp.servico).toEqual(servico);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServico>>();
      const servico = { id: 7799 };
      jest.spyOn(servicoFormService, 'getServico').mockReturnValue(servico);
      jest.spyOn(servicoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: servico }));
      saveSubject.complete();

      // THEN
      expect(servicoFormService.getServico).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicoService.update).toHaveBeenCalledWith(expect.objectContaining(servico));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServico>>();
      const servico = { id: 7799 };
      jest.spyOn(servicoFormService, 'getServico').mockReturnValue({ id: null });
      jest.spyOn(servicoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servico: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: servico }));
      saveSubject.complete();

      // THEN
      expect(servicoFormService.getServico).toHaveBeenCalled();
      expect(servicoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServico>>();
      const servico = { id: 7799 };
      jest.spyOn(servicoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
