import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAula } from 'app/entities/aula/aula.model';
import { AulaService } from 'app/entities/aula/service/aula.service';
import { IProfessor } from 'app/entities/professor/professor.model';
import { ProfessorService } from 'app/entities/professor/service/professor.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IMateria } from '../materia.model';
import { MateriaService } from '../service/materia.service';
import { MateriaFormService } from './materia-form.service';

import { MateriaUpdateComponent } from './materia-update.component';

describe('Materia Management Update Component', () => {
  let comp: MateriaUpdateComponent;
  let fixture: ComponentFixture<MateriaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materiaFormService: MateriaFormService;
  let materiaService: MateriaService;
  let aulaService: AulaService;
  let professorService: ProfessorService;
  let cursoService: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MateriaUpdateComponent],
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
      .overrideTemplate(MateriaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MateriaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materiaFormService = TestBed.inject(MateriaFormService);
    materiaService = TestBed.inject(MateriaService);
    aulaService = TestBed.inject(AulaService);
    professorService = TestBed.inject(ProfessorService);
    cursoService = TestBed.inject(CursoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Aula query and add missing value', () => {
      const materia: IMateria = { id: 7681 };
      const aulas: IAula[] = [{ id: 31179 }];
      materia.aulas = aulas;

      const aulaCollection: IAula[] = [{ id: 31179 }];
      jest.spyOn(aulaService, 'query').mockReturnValue(of(new HttpResponse({ body: aulaCollection })));
      const additionalAulas = [...aulas];
      const expectedCollection: IAula[] = [...additionalAulas, ...aulaCollection];
      jest.spyOn(aulaService, 'addAulaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      expect(aulaService.query).toHaveBeenCalled();
      expect(aulaService.addAulaToCollectionIfMissing).toHaveBeenCalledWith(
        aulaCollection,
        ...additionalAulas.map(expect.objectContaining),
      );
      expect(comp.aulasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Professor query and add missing value', () => {
      const materia: IMateria = { id: 7681 };
      const professor: IProfessor = { id: 2234 };
      materia.professor = professor;

      const professorCollection: IProfessor[] = [{ id: 2234 }];
      jest.spyOn(professorService, 'query').mockReturnValue(of(new HttpResponse({ body: professorCollection })));
      const additionalProfessors = [professor];
      const expectedCollection: IProfessor[] = [...additionalProfessors, ...professorCollection];
      jest.spyOn(professorService, 'addProfessorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      expect(professorService.query).toHaveBeenCalled();
      expect(professorService.addProfessorToCollectionIfMissing).toHaveBeenCalledWith(
        professorCollection,
        ...additionalProfessors.map(expect.objectContaining),
      );
      expect(comp.professorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Curso query and add missing value', () => {
      const materia: IMateria = { id: 7681 };
      const cursos: ICurso[] = [{ id: 1874 }];
      materia.cursos = cursos;

      const cursoCollection: ICurso[] = [{ id: 1874 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [...cursos];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(
        cursoCollection,
        ...additionalCursos.map(expect.objectContaining),
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materia: IMateria = { id: 7681 };
      const aula: IAula = { id: 31179 };
      materia.aulas = [aula];
      const professor: IProfessor = { id: 2234 };
      materia.professor = professor;
      const curso: ICurso = { id: 1874 };
      materia.cursos = [curso];

      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      expect(comp.aulasSharedCollection).toContainEqual(aula);
      expect(comp.professorsSharedCollection).toContainEqual(professor);
      expect(comp.cursosSharedCollection).toContainEqual(curso);
      expect(comp.materia).toEqual(materia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateria>>();
      const materia = { id: 23511 };
      jest.spyOn(materiaFormService, 'getMateria').mockReturnValue(materia);
      jest.spyOn(materiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materia }));
      saveSubject.complete();

      // THEN
      expect(materiaFormService.getMateria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materiaService.update).toHaveBeenCalledWith(expect.objectContaining(materia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateria>>();
      const materia = { id: 23511 };
      jest.spyOn(materiaFormService, 'getMateria').mockReturnValue({ id: null });
      jest.spyOn(materiaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materia }));
      saveSubject.complete();

      // THEN
      expect(materiaFormService.getMateria).toHaveBeenCalled();
      expect(materiaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateria>>();
      const materia = { id: 23511 };
      jest.spyOn(materiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materiaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAula', () => {
      it('Should forward to aulaService', () => {
        const entity = { id: 31179 };
        const entity2 = { id: 11398 };
        jest.spyOn(aulaService, 'compareAula');
        comp.compareAula(entity, entity2);
        expect(aulaService.compareAula).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProfessor', () => {
      it('Should forward to professorService', () => {
        const entity = { id: 2234 };
        const entity2 = { id: 26501 };
        jest.spyOn(professorService, 'compareProfessor');
        comp.compareProfessor(entity, entity2);
        expect(professorService.compareProfessor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCurso', () => {
      it('Should forward to cursoService', () => {
        const entity = { id: 1874 };
        const entity2 = { id: 13472 };
        jest.spyOn(cursoService, 'compareCurso');
        comp.compareCurso(entity, entity2);
        expect(cursoService.compareCurso).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
