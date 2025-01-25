import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPais, NewPais } from '../pais.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPais for edit and NewPaisFormGroupInput for create.
 */
type PaisFormGroupInput = IPais | PartialWithRequiredKeyOf<NewPais>;

type PaisFormDefaults = Pick<NewPais, 'id'>;

type PaisFormGroupContent = {
  id: FormControl<IPais['id'] | NewPais['id']>;
  nomeDoPais: FormControl<IPais['nomeDoPais']>;
  local: FormControl<IPais['local']>;
};

export type PaisFormGroup = FormGroup<PaisFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaisFormService {
  createPaisFormGroup(pais: PaisFormGroupInput = { id: null }): PaisFormGroup {
    const paisRawValue = {
      ...this.getFormDefaults(),
      ...pais,
    };
    return new FormGroup<PaisFormGroupContent>({
      id: new FormControl(
        { value: paisRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomeDoPais: new FormControl(paisRawValue.nomeDoPais),
      local: new FormControl(paisRawValue.local),
    });
  }

  getPais(form: PaisFormGroup): IPais | NewPais {
    return form.getRawValue() as IPais | NewPais;
  }

  resetForm(form: PaisFormGroup, pais: PaisFormGroupInput): void {
    const paisRawValue = { ...this.getFormDefaults(), ...pais };
    form.reset(
      {
        ...paisRawValue,
        id: { value: paisRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaisFormDefaults {
    return {
      id: null,
    };
  }
}
