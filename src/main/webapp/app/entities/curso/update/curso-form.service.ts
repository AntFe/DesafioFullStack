import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICurso, NewCurso } from '../curso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICurso for edit and NewCursoFormGroupInput for create.
 */
type CursoFormGroupInput = ICurso | PartialWithRequiredKeyOf<NewCurso>;

type CursoFormDefaults = Pick<NewCurso, 'id' | 'materias' | 'alunos'>;

type CursoFormGroupContent = {
  id: FormControl<ICurso['id'] | NewCurso['id']>;
  nome: FormControl<ICurso['nome']>;
  materias: FormControl<ICurso['materias']>;
  alunos: FormControl<ICurso['alunos']>;
};

export type CursoFormGroup = FormGroup<CursoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CursoFormService {
  createCursoFormGroup(curso: CursoFormGroupInput = { id: null }): CursoFormGroup {
    const cursoRawValue = {
      ...this.getFormDefaults(),
      ...curso,
    };
    return new FormGroup<CursoFormGroupContent>({
      id: new FormControl(
        { value: cursoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(cursoRawValue.nome, {
        validators: [Validators.required],
      }),
      materias: new FormControl(cursoRawValue.materias ?? []),
      alunos: new FormControl(cursoRawValue.alunos ?? []),
    });
  }

  getCurso(form: CursoFormGroup): ICurso | NewCurso {
    return form.getRawValue() as ICurso | NewCurso;
  }

  resetForm(form: CursoFormGroup, curso: CursoFormGroupInput): void {
    const cursoRawValue = { ...this.getFormDefaults(), ...curso };
    form.reset(
      {
        ...cursoRawValue,
        id: { value: cursoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CursoFormDefaults {
    return {
      id: null,
      materias: [],
      alunos: [],
    };
  }
}
