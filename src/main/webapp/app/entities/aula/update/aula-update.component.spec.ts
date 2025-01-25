import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMateria } from 'app/entities/materia/materia.model';
import { MateriaService } from 'app/entities/materia/service/materia.service';
import { AulaService } from '../service/aula.service';
import { IAula } from '../aula.model';
import { AulaFormService } from './aula-form.service';

import { AulaUpdateComponent } from './aula-update.component';

describe('Aula Management Update Component', () => {
  let comp: AulaUpdateComponent;
  let fixture: ComponentFixture<AulaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aulaFormService: AulaFormService;
  let aulaService: AulaService;
  let materiaService: MateriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AulaUpdateComponent],
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
      .overrideTemplate(AulaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AulaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aulaFormService = TestBed.inject(AulaFormService);
    aulaService = TestBed.inject(AulaService);
    materiaService = TestBed.inject(MateriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Materia query and add missing value', () => {
      const aula: IAula = { id: 11398 };
      const materias: IMateria[] = [{ id: 23511 }];
      aula.materias = materias;

      const materiaCollection: IMateria[] = [{ id: 23511 }];
      jest.spyOn(materiaService, 'query').mockReturnValue(of(new HttpResponse({ body: materiaCollection })));
      const additionalMaterias = [...materias];
      const expectedCollection: IMateria[] = [...additionalMaterias, ...materiaCollection];
      jest.spyOn(materiaService, 'addMateriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aula });
      comp.ngOnInit();

      expect(materiaService.query).toHaveBeenCalled();
      expect(materiaService.addMateriaToCollectionIfMissing).toHaveBeenCalledWith(
        materiaCollection,
        ...additionalMaterias.map(expect.objectContaining),
      );
      expect(comp.materiasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aula: IAula = { id: 11398 };
      const materia: IMateria = { id: 23511 };
      aula.materias = [materia];

      activatedRoute.data = of({ aula });
      comp.ngOnInit();

      expect(comp.materiasSharedCollection).toContainEqual(materia);
      expect(comp.aula).toEqual(aula);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAula>>();
      const aula = { id: 31179 };
      jest.spyOn(aulaFormService, 'getAula').mockReturnValue(aula);
      jest.spyOn(aulaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aula }));
      saveSubject.complete();

      // THEN
      expect(aulaFormService.getAula).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aulaService.update).toHaveBeenCalledWith(expect.objectContaining(aula));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAula>>();
      const aula = { id: 31179 };
      jest.spyOn(aulaFormService, 'getAula').mockReturnValue({ id: null });
      jest.spyOn(aulaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aula: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aula }));
      saveSubject.complete();

      // THEN
      expect(aulaFormService.getAula).toHaveBeenCalled();
      expect(aulaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAula>>();
      const aula = { id: 31179 };
      jest.spyOn(aulaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aulaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMateria', () => {
      it('Should forward to materiaService', () => {
        const entity = { id: 23511 };
        const entity2 = { id: 7681 };
        jest.spyOn(materiaService, 'compareMateria');
        comp.compareMateria(entity, entity2);
        expect(materiaService.compareMateria).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
