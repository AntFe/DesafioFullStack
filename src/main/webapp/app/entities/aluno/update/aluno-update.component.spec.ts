import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IAluno } from '../aluno.model';
import { AlunoService } from '../service/aluno.service';
import { AlunoFormService } from './aluno-form.service';

import { AlunoUpdateComponent } from './aluno-update.component';

describe('Aluno Management Update Component', () => {
  let comp: AlunoUpdateComponent;
  let fixture: ComponentFixture<AlunoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alunoFormService: AlunoFormService;
  let alunoService: AlunoService;
  let enderecoService: EnderecoService;
  let cursoService: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlunoUpdateComponent],
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
      .overrideTemplate(AlunoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlunoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alunoFormService = TestBed.inject(AlunoFormService);
    alunoService = TestBed.inject(AlunoService);
    enderecoService = TestBed.inject(EnderecoService);
    cursoService = TestBed.inject(CursoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call endereco query and add missing value', () => {
      const aluno: IAluno = { id: 9303 };
      const endereco: IEndereco = { id: 42 };
      aluno.endereco = endereco;

      const enderecoCollection: IEndereco[] = [{ id: 42 }];
      jest.spyOn(enderecoService, 'query').mockReturnValue(of(new HttpResponse({ body: enderecoCollection })));
      const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
      jest.spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      expect(enderecoService.query).toHaveBeenCalled();
      expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
      expect(comp.enderecosCollection).toEqual(expectedCollection);
    });

    it('Should call Curso query and add missing value', () => {
      const aluno: IAluno = { id: 9303 };
      const cursos: ICurso[] = [{ id: 1874 }];
      aluno.cursos = cursos;

      const cursoCollection: ICurso[] = [{ id: 1874 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [...cursos];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(
        cursoCollection,
        ...additionalCursos.map(expect.objectContaining),
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aluno: IAluno = { id: 9303 };
      const endereco: IEndereco = { id: 42 };
      aluno.endereco = endereco;
      const curso: ICurso = { id: 1874 };
      aluno.cursos = [curso];

      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      expect(comp.enderecosCollection).toContainEqual(endereco);
      expect(comp.cursosSharedCollection).toContainEqual(curso);
      expect(comp.aluno).toEqual(aluno);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAluno>>();
      const aluno = { id: 15328 };
      jest.spyOn(alunoFormService, 'getAluno').mockReturnValue(aluno);
      jest.spyOn(alunoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aluno }));
      saveSubject.complete();

      // THEN
      expect(alunoFormService.getAluno).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alunoService.update).toHaveBeenCalledWith(expect.objectContaining(aluno));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAluno>>();
      const aluno = { id: 15328 };
      jest.spyOn(alunoFormService, 'getAluno').mockReturnValue({ id: null });
      jest.spyOn(alunoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aluno: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aluno }));
      saveSubject.complete();

      // THEN
      expect(alunoFormService.getAluno).toHaveBeenCalled();
      expect(alunoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAluno>>();
      const aluno = { id: 15328 };
      jest.spyOn(alunoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alunoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEndereco', () => {
      it('Should forward to enderecoService', () => {
        const entity = { id: 42 };
        const entity2 = { id: 870 };
        jest.spyOn(enderecoService, 'compareEndereco');
        comp.compareEndereco(entity, entity2);
        expect(enderecoService.compareEndereco).toHaveBeenCalledWith(entity, entity2);
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
