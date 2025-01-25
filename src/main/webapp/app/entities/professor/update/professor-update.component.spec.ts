import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IServico } from 'app/entities/servico/servico.model';
import { ServicoService } from 'app/entities/servico/service/servico.service';
import { ProfessorService } from '../service/professor.service';
import { IProfessor } from '../professor.model';
import { ProfessorFormService } from './professor-form.service';

import { ProfessorUpdateComponent } from './professor-update.component';

describe('Professor Management Update Component', () => {
  let comp: ProfessorUpdateComponent;
  let fixture: ComponentFixture<ProfessorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let professorFormService: ProfessorFormService;
  let professorService: ProfessorService;
  let servicoService: ServicoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfessorUpdateComponent],
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
      .overrideTemplate(ProfessorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfessorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    professorFormService = TestBed.inject(ProfessorFormService);
    professorService = TestBed.inject(ProfessorService);
    servicoService = TestBed.inject(ServicoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call servico query and add missing value', () => {
      const professor: IProfessor = { id: 26501 };
      const servico: IServico = { id: 7799 };
      professor.servico = servico;

      const servicoCollection: IServico[] = [{ id: 7799 }];
      jest.spyOn(servicoService, 'query').mockReturnValue(of(new HttpResponse({ body: servicoCollection })));
      const expectedCollection: IServico[] = [servico, ...servicoCollection];
      jest.spyOn(servicoService, 'addServicoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      expect(servicoService.query).toHaveBeenCalled();
      expect(servicoService.addServicoToCollectionIfMissing).toHaveBeenCalledWith(servicoCollection, servico);
      expect(comp.servicosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const professor: IProfessor = { id: 26501 };
      const servico: IServico = { id: 7799 };
      professor.servico = servico;

      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      expect(comp.servicosCollection).toContainEqual(servico);
      expect(comp.professor).toEqual(professor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessor>>();
      const professor = { id: 2234 };
      jest.spyOn(professorFormService, 'getProfessor').mockReturnValue(professor);
      jest.spyOn(professorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: professor }));
      saveSubject.complete();

      // THEN
      expect(professorFormService.getProfessor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(professorService.update).toHaveBeenCalledWith(expect.objectContaining(professor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessor>>();
      const professor = { id: 2234 };
      jest.spyOn(professorFormService, 'getProfessor').mockReturnValue({ id: null });
      jest.spyOn(professorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: professor }));
      saveSubject.complete();

      // THEN
      expect(professorFormService.getProfessor).toHaveBeenCalled();
      expect(professorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessor>>();
      const professor = { id: 2234 };
      jest.spyOn(professorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(professorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServico', () => {
      it('Should forward to servicoService', () => {
        const entity = { id: 7799 };
        const entity2 = { id: 14176 };
        jest.spyOn(servicoService, 'compareServico');
        comp.compareServico(entity, entity2);
        expect(servicoService.compareServico).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
