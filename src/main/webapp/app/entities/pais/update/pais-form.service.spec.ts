import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pais.test-samples';

import { PaisFormService } from './pais-form.service';

describe('Pais Form Service', () => {
  let service: PaisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaisFormService);
  });

  describe('Service methods', () => {
    describe('createPaisFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaisFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeDoPais: expect.any(Object),
            local: expect.any(Object),
          }),
        );
      });

      it('passing IPais should create a new form with FormGroup', () => {
        const formGroup = service.createPaisFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeDoPais: expect.any(Object),
            local: expect.any(Object),
          }),
        );
      });
    });

    describe('getPais', () => {
      it('should return NewPais for default Pais initial value', () => {
        const formGroup = service.createPaisFormGroup(sampleWithNewData);

        const pais = service.getPais(formGroup) as any;

        expect(pais).toMatchObject(sampleWithNewData);
      });

      it('should return NewPais for empty Pais initial value', () => {
        const formGroup = service.createPaisFormGroup();

        const pais = service.getPais(formGroup) as any;

        expect(pais).toMatchObject({});
      });

      it('should return IPais', () => {
        const formGroup = service.createPaisFormGroup(sampleWithRequiredData);

        const pais = service.getPais(formGroup) as any;

        expect(pais).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPais should not enable id FormControl', () => {
        const formGroup = service.createPaisFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPais should disable id FormControl', () => {
        const formGroup = service.createPaisFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
