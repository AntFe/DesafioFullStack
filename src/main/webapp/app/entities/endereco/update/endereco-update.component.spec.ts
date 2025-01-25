import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';
import { EnderecoService } from '../service/endereco.service';
import { IEndereco } from '../endereco.model';
import { EnderecoFormService } from './endereco-form.service';

import { EnderecoUpdateComponent } from './endereco-update.component';

describe('Endereco Management Update Component', () => {
  let comp: EnderecoUpdateComponent;
  let fixture: ComponentFixture<EnderecoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enderecoFormService: EnderecoFormService;
  let enderecoService: EnderecoService;
  let paisService: PaisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EnderecoUpdateComponent],
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
      .overrideTemplate(EnderecoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnderecoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enderecoFormService = TestBed.inject(EnderecoFormService);
    enderecoService = TestBed.inject(EnderecoService);
    paisService = TestBed.inject(PaisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call pais query and add missing value', () => {
      const endereco: IEndereco = { id: 870 };
      const pais: IPais = { id: 17868 };
      endereco.pais = pais;

      const paisCollection: IPais[] = [{ id: 17868 }];
      jest.spyOn(paisService, 'query').mockReturnValue(of(new HttpResponse({ body: paisCollection })));
      const expectedCollection: IPais[] = [pais, ...paisCollection];
      jest.spyOn(paisService, 'addPaisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(paisService.query).toHaveBeenCalled();
      expect(paisService.addPaisToCollectionIfMissing).toHaveBeenCalledWith(paisCollection, pais);
      expect(comp.paisCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const endereco: IEndereco = { id: 870 };
      const pais: IPais = { id: 17868 };
      endereco.pais = pais;

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(comp.paisCollection).toContainEqual(pais);
      expect(comp.endereco).toEqual(endereco);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndereco>>();
      const endereco = { id: 42 };
      jest.spyOn(enderecoFormService, 'getEndereco').mockReturnValue(endereco);
      jest.spyOn(enderecoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: endereco }));
      saveSubject.complete();

      // THEN
      expect(enderecoFormService.getEndereco).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enderecoService.update).toHaveBeenCalledWith(expect.objectContaining(endereco));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndereco>>();
      const endereco = { id: 42 };
      jest.spyOn(enderecoFormService, 'getEndereco').mockReturnValue({ id: null });
      jest.spyOn(enderecoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endereco: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: endereco }));
      saveSubject.complete();

      // THEN
      expect(enderecoFormService.getEndereco).toHaveBeenCalled();
      expect(enderecoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndereco>>();
      const endereco = { id: 42 };
      jest.spyOn(enderecoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enderecoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePais', () => {
      it('Should forward to paisService', () => {
        const entity = { id: 17868 };
        const entity2 = { id: 16774 };
        jest.spyOn(paisService, 'comparePais');
        comp.comparePais(entity, entity2);
        expect(paisService.comparePais).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
