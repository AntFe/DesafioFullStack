import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMateria, NewMateria } from '../materia.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMateria for edit and NewMateriaFormGroupInput for create.
 */
type MateriaFormGroupInput = IMateria | PartialWithRequiredKeyOf<NewMateria>;

type MateriaFormDefaults = Pick<NewMateria, 'id' | 'aulas' | 'cursos'>;

type MateriaFormGroupContent = {
  id: FormControl<IMateria['id'] | NewMateria['id']>;
  nomeDaMateria: FormControl<IMateria['nomeDaMateria']>;
  ementa: FormControl<IMateria['ementa']>;
  referenciasBibliograficas: FormControl<IMateria['referenciasBibliograficas']>;
  aulas: FormControl<IMateria['aulas']>;
  professor: FormControl<IMateria['professor']>;
  cursos: FormControl<IMateria['cursos']>;
};

export type MateriaFormGroup = FormGroup<MateriaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MateriaFormService {
  createMateriaFormGroup(materia: MateriaFormGroupInput = { id: null }): MateriaFormGroup {
    const materiaRawValue = {
      ...this.getFormDefaults(),
      ...materia,
    };
    return new FormGroup<MateriaFormGroupContent>({
      id: new FormControl(
        { value: materiaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomeDaMateria: new FormControl(materiaRawValue.nomeDaMateria),
      ementa: new FormControl(materiaRawValue.ementa),
      referenciasBibliograficas: new FormControl(materiaRawValue.referenciasBibliograficas),
      aulas: new FormControl(materiaRawValue.aulas ?? []),
      professor: new FormControl(materiaRawValue.professor),
      cursos: new FormControl(materiaRawValue.cursos ?? []),
    });
  }

  getMateria(form: MateriaFormGroup): IMateria | NewMateria {
    return form.getRawValue() as IMateria | NewMateria;
  }

  resetForm(form: MateriaFormGroup, materia: MateriaFormGroupInput): void {
    const materiaRawValue = { ...this.getFormDefaults(), ...materia };
    form.reset(
      {
        ...materiaRawValue,
        id: { value: materiaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MateriaFormDefaults {
    return {
      id: null,
      aulas: [],
      cursos: [],
    };
  }
}
