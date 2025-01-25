import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../aula.test-samples';

import { AulaFormService } from './aula-form.service';

describe('Aula Form Service', () => {
  let service: AulaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AulaFormService);
  });

  describe('Service methods', () => {
    describe('createAulaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAulaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tituloAula: expect.any(Object),
            descricao: expect.any(Object),
            linkVideo: expect.any(Object),
            linkArquivos: expect.any(Object),
            resumo: expect.any(Object),
            materias: expect.any(Object),
          }),
        );
      });

      it('passing IAula should create a new form with FormGroup', () => {
        const formGroup = service.createAulaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tituloAula: expect.any(Object),
            descricao: expect.any(Object),
            linkVideo: expect.any(Object),
            linkArquivos: expect.any(Object),
            resumo: expect.any(Object),
            materias: expect.any(Object),
          }),
        );
      });
    });

    describe('getAula', () => {
      it('should return NewAula for default Aula initial value', () => {
        const formGroup = service.createAulaFormGroup(sampleWithNewData);

        const aula = service.getAula(formGroup) as any;

        expect(aula).toMatchObject(sampleWithNewData);
      });

      it('should return NewAula for empty Aula initial value', () => {
        const formGroup = service.createAulaFormGroup();

        const aula = service.getAula(formGroup) as any;

        expect(aula).toMatchObject({});
      });

      it('should return IAula', () => {
        const formGroup = service.createAulaFormGroup(sampleWithRequiredData);

        const aula = service.getAula(formGroup) as any;

        expect(aula).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAula should not enable id FormControl', () => {
        const formGroup = service.createAulaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAula should disable id FormControl', () => {
        const formGroup = service.createAulaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
