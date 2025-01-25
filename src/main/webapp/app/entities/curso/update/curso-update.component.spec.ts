import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMateria } from 'app/entities/materia/materia.model';
import { MateriaService } from 'app/entities/materia/service/materia.service';
import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { ICurso } from '../curso.model';
import { CursoService } from '../service/curso.service';
import { CursoFormService } from './curso-form.service';

import { CursoUpdateComponent } from './curso-update.component';

describe('Curso Management Update Component', () => {
  let comp: CursoUpdateComponent;
  let fixture: ComponentFixture<CursoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cursoFormService: CursoFormService;
  let cursoService: CursoService;
  let materiaService: MateriaService;
  let alunoService: AlunoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CursoUpdateComponent],
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
      .overrideTemplate(CursoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CursoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cursoFormService = TestBed.inject(CursoFormService);
    cursoService = TestBed.inject(CursoService);
    materiaService = TestBed.inject(MateriaService);
    alunoService = TestBed.inject(AlunoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Materia query and add missing value', () => {
      const curso: ICurso = { id: 13472 };
      const materias: IMateria[] = [{ id: 23511 }];
      curso.materias = materias;

      const materiaCollection: IMateria[] = [{ id: 23511 }];
      jest.spyOn(materiaService, 'query').mockReturnValue(of(new HttpResponse({ body: materiaCollection })));
      const additionalMaterias = [...materias];
      const expectedCollection: IMateria[] = [...additionalMaterias, ...materiaCollection];
      jest.spyOn(materiaService, 'addMateriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(materiaService.query).toHaveBeenCalled();
      expect(materiaService.addMateriaToCollectionIfMissing).toHaveBeenCalledWith(
        materiaCollection,
        ...additionalMaterias.map(expect.objectContaining),
      );
      expect(comp.materiasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Aluno query and add missing value', () => {
      const curso: ICurso = { id: 13472 };
      const alunos: IAluno[] = [{ id: 15328 }];
      curso.alunos = alunos;

      const alunoCollection: IAluno[] = [{ id: 15328 }];
      jest.spyOn(alunoService, 'query').mockReturnValue(of(new HttpResponse({ body: alunoCollection })));
      const additionalAlunos = [...alunos];
      const expectedCollection: IAluno[] = [...additionalAlunos, ...alunoCollection];
      jest.spyOn(alunoService, 'addAlunoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(alunoService.query).toHaveBeenCalled();
      expect(alunoService.addAlunoToCollectionIfMissing).toHaveBeenCalledWith(
        alunoCollection,
        ...additionalAlunos.map(expect.objectContaining),
      );
      expect(comp.alunosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const curso: ICurso = { id: 13472 };
      const materia: IMateria = { id: 23511 };
      curso.materias = [materia];
      const aluno: IAluno = { id: 15328 };
      curso.alunos = [aluno];

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(comp.materiasSharedCollection).toContainEqual(materia);
      expect(comp.alunosSharedCollection).toContainEqual(aluno);
      expect(comp.curso).toEqual(curso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 1874 };
      jest.spyOn(cursoFormService, 'getCurso').mockReturnValue(curso);
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoFormService.getCurso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cursoService.update).toHaveBeenCalledWith(expect.objectContaining(curso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 1874 };
      jest.spyOn(cursoFormService, 'getCurso').mockReturnValue({ id: null });
      jest.spyOn(cursoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoFormService.getCurso).toHaveBeenCalled();
      expect(cursoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 1874 };
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cursoService.update).toHaveBeenCalled();
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

    describe('compareAluno', () => {
      it('Should forward to alunoService', () => {
        const entity = { id: 15328 };
        const entity2 = { id: 9303 };
        jest.spyOn(alunoService, 'compareAluno');
        comp.compareAluno(entity, entity2);
        expect(alunoService.compareAluno).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
