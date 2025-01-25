import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILocal } from 'app/entities/local/local.model';
import { LocalService } from 'app/entities/local/service/local.service';
import { PaisService } from '../service/pais.service';
import { IPais } from '../pais.model';
import { PaisFormService } from './pais-form.service';

import { PaisUpdateComponent } from './pais-update.component';

describe('Pais Management Update Component', () => {
  let comp: PaisUpdateComponent;
  let fixture: ComponentFixture<PaisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paisFormService: PaisFormService;
  let paisService: PaisService;
  let localService: LocalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PaisUpdateComponent],
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
      .overrideTemplate(PaisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paisFormService = TestBed.inject(PaisFormService);
    paisService = TestBed.inject(PaisService);
    localService = TestBed.inject(LocalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call local query and add missing value', () => {
      const pais: IPais = { id: 16774 };
      const local: ILocal = { id: 3747 };
      pais.local = local;

      const localCollection: ILocal[] = [{ id: 3747 }];
      jest.spyOn(localService, 'query').mockReturnValue(of(new HttpResponse({ body: localCollection })));
      const expectedCollection: ILocal[] = [local, ...localCollection];
      jest.spyOn(localService, 'addLocalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      expect(localService.query).toHaveBeenCalled();
      expect(localService.addLocalToCollectionIfMissing).toHaveBeenCalledWith(localCollection, local);
      expect(comp.localsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pais: IPais = { id: 16774 };
      const local: ILocal = { id: 3747 };
      pais.local = local;

      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      expect(comp.localsCollection).toContainEqual(local);
      expect(comp.pais).toEqual(pais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 17868 };
      jest.spyOn(paisFormService, 'getPais').mockReturnValue(pais);
      jest.spyOn(paisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pais }));
      saveSubject.complete();

      // THEN
      expect(paisFormService.getPais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paisService.update).toHaveBeenCalledWith(expect.objectContaining(pais));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 17868 };
      jest.spyOn(paisFormService, 'getPais').mockReturnValue({ id: null });
      jest.spyOn(paisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pais }));
      saveSubject.complete();

      // THEN
      expect(paisFormService.getPais).toHaveBeenCalled();
      expect(paisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 17868 };
      jest.spyOn(paisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocal', () => {
      it('Should forward to localService', () => {
        const entity = { id: 3747 };
        const entity2 = { id: 29517 };
        jest.spyOn(localService, 'compareLocal');
        comp.compareLocal(entity, entity2);
        expect(localService.compareLocal).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
