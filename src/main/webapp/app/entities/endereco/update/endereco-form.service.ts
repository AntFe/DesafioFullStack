import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEndereco, NewEndereco } from '../endereco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEndereco for edit and NewEnderecoFormGroupInput for create.
 */
type EnderecoFormGroupInput = IEndereco | PartialWithRequiredKeyOf<NewEndereco>;

type EnderecoFormDefaults = Pick<NewEndereco, 'id'>;

type EnderecoFormGroupContent = {
  id: FormControl<IEndereco['id'] | NewEndereco['id']>;
  rua: FormControl<IEndereco['rua']>;
  cep: FormControl<IEndereco['cep']>;
  cidade: FormControl<IEndereco['cidade']>;
  estado: FormControl<IEndereco['estado']>;
  pais: FormControl<IEndereco['pais']>;
};

export type EnderecoFormGroup = FormGroup<EnderecoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnderecoFormService {
  createEnderecoFormGroup(endereco: EnderecoFormGroupInput = { id: null }): EnderecoFormGroup {
    const enderecoRawValue = {
      ...this.getFormDefaults(),
      ...endereco,
    };
    return new FormGroup<EnderecoFormGroupContent>({
      id: new FormControl(
        { value: enderecoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rua: new FormControl(enderecoRawValue.rua),
      cep: new FormControl(enderecoRawValue.cep),
      cidade: new FormControl(enderecoRawValue.cidade),
      estado: new FormControl(enderecoRawValue.estado),
      pais: new FormControl(enderecoRawValue.pais),
    });
  }

  getEndereco(form: EnderecoFormGroup): IEndereco | NewEndereco {
    return form.getRawValue() as IEndereco | NewEndereco;
  }

  resetForm(form: EnderecoFormGroup, endereco: EnderecoFormGroupInput): void {
    const enderecoRawValue = { ...this.getFormDefaults(), ...endereco };
    form.reset(
      {
        ...enderecoRawValue,
        id: { value: enderecoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnderecoFormDefaults {
    return {
      id: null,
    };
  }
}
