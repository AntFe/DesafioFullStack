import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../materia.test-samples';

import { MateriaFormService } from './materia-form.service';

describe('Materia Form Service', () => {
  let service: MateriaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MateriaFormService);
  });

  describe('Service methods', () => {
    describe('createMateriaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMateriaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeDaMateria: expect.any(Object),
            ementa: expect.any(Object),
            referenciasBibliograficas: expect.any(Object),
            aulas: expect.any(Object),
            professor: expect.any(Object),
            cursos: expect.any(Object),
          }),
        );
      });

      it('passing IMateria should create a new form with FormGroup', () => {
        const formGroup = service.createMateriaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeDaMateria: expect.any(Object),
            ementa: expect.any(Object),
            referenciasBibliograficas: expect.any(Object),
            aulas: expect.any(Object),
            professor: expect.any(Object),
            cursos: expect.any(Object),
          }),
        );
      });
    });

    describe('getMateria', () => {
      it('should return NewMateria for default Materia initial value', () => {
        const formGroup = service.createMateriaFormGroup(sampleWithNewData);

        const materia = service.getMateria(formGroup) as any;

        expect(materia).toMatchObject(sampleWithNewData);
      });

      it('should return NewMateria for empty Materia initial value', () => {
        const formGroup = service.createMateriaFormGroup();

        const materia = service.getMateria(formGroup) as any;

        expect(materia).toMatchObject({});
      });

      it('should return IMateria', () => {
        const formGroup = service.createMateriaFormGroup(sampleWithRequiredData);

        const materia = service.getMateria(formGroup) as any;

        expect(materia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMateria should not enable id FormControl', () => {
        const formGroup = service.createMateriaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMateria should disable id FormControl', () => {
        const formGroup = service.createMateriaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
