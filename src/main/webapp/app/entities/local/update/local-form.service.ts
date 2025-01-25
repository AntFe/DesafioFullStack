import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILocal, NewLocal } from '../local.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocal for edit and NewLocalFormGroupInput for create.
 */
type LocalFormGroupInput = ILocal | PartialWithRequiredKeyOf<NewLocal>;

type LocalFormDefaults = Pick<NewLocal, 'id'>;

type LocalFormGroupContent = {
  id: FormControl<ILocal['id'] | NewLocal['id']>;
  nomeDoLocal: FormControl<ILocal['nomeDoLocal']>;
};

export type LocalFormGroup = FormGroup<LocalFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocalFormService {
  createLocalFormGroup(local: LocalFormGroupInput = { id: null }): LocalFormGroup {
    const localRawValue = {
      ...this.getFormDefaults(),
      ...local,
    };
    return new FormGroup<LocalFormGroupContent>({
      id: new FormControl(
        { value: localRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomeDoLocal: new FormControl(localRawValue.nomeDoLocal),
    });
  }

  getLocal(form: LocalFormGroup): ILocal | NewLocal {
    return form.getRawValue() as ILocal | NewLocal;
  }

  resetForm(form: LocalFormGroup, local: LocalFormGroupInput): void {
    const localRawValue = { ...this.getFormDefaults(), ...local };
    form.reset(
      {
        ...localRawValue,
        id: { value: localRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocalFormDefaults {
    return {
      id: null,
    };
  }
}
