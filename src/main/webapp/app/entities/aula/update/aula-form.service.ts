import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAula, NewAula } from '../aula.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAula for edit and NewAulaFormGroupInput for create.
 */
type AulaFormGroupInput = IAula | PartialWithRequiredKeyOf<NewAula>;

type AulaFormDefaults = Pick<NewAula, 'id' | 'materias'>;

type AulaFormGroupContent = {
  id: FormControl<IAula['id'] | NewAula['id']>;
  tituloAula: FormControl<IAula['tituloAula']>;
  descricao: FormControl<IAula['descricao']>;
  linkVideo: FormControl<IAula['linkVideo']>;
  linkArquivos: FormControl<IAula['linkArquivos']>;
  resumo: FormControl<IAula['resumo']>;
  materias: FormControl<IAula['materias']>;
};

export type AulaFormGroup = FormGroup<AulaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AulaFormService {
  createAulaFormGroup(aula: AulaFormGroupInput = { id: null }): AulaFormGroup {
    const aulaRawValue = {
      ...this.getFormDefaults(),
      ...aula,
    };
    return new FormGroup<AulaFormGroupContent>({
      id: new FormControl(
        { value: aulaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tituloAula: new FormControl(aulaRawValue.tituloAula),
      descricao: new FormControl(aulaRawValue.descricao),
      linkVideo: new FormControl(aulaRawValue.linkVideo),
      linkArquivos: new FormControl(aulaRawValue.linkArquivos),
      resumo: new FormControl(aulaRawValue.resumo),
      materias: new FormControl(aulaRawValue.materias ?? []),
    });
  }

  getAula(form: AulaFormGroup): IAula | NewAula {
    return form.getRawValue() as IAula | NewAula;
  }

  resetForm(form: AulaFormGroup, aula: AulaFormGroupInput): void {
    const aulaRawValue = { ...this.getFormDefaults(), ...aula };
    form.reset(
      {
        ...aulaRawValue,
        id: { value: aulaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AulaFormDefaults {
    return {
      id: null,
      materias: [],
    };
  }
}
