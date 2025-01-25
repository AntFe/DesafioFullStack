import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../servico.test-samples';

import { ServicoFormService } from './servico-form.service';

describe('Servico Form Service', () => {
  let service: ServicoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicoFormService);
  });

  describe('Service methods', () => {
    describe('createServicoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServicoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            nomeDoServico: expect.any(Object),
            resumo: expect.any(Object),
          }),
        );
      });

      it('passing IServico should create a new form with FormGroup', () => {
        const formGroup = service.createServicoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            nomeDoServico: expect.any(Object),
            resumo: expect.any(Object),
          }),
        );
      });
    });

    describe('getServico', () => {
      it('should return NewServico for default Servico initial value', () => {
        const formGroup = service.createServicoFormGroup(sampleWithNewData);

        const servico = service.getServico(formGroup) as any;

        expect(servico).toMatchObject(sampleWithNewData);
      });

      it('should return NewServico for empty Servico initial value', () => {
        const formGroup = service.createServicoFormGroup();

        const servico = service.getServico(formGroup) as any;

        expect(servico).toMatchObject({});
      });

      it('should return IServico', () => {
        const formGroup = service.createServicoFormGroup(sampleWithRequiredData);

        const servico = service.getServico(formGroup) as any;

        expect(servico).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServico should not enable id FormControl', () => {
        const formGroup = service.createServicoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServico should disable id FormControl', () => {
        const formGroup = service.createServicoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
