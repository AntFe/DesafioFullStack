import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServico, NewServico } from '../servico.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServico for edit and NewServicoFormGroupInput for create.
 */
type ServicoFormGroupInput = IServico | PartialWithRequiredKeyOf<NewServico>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServico | NewServico> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ServicoFormRawValue = FormValueOf<IServico>;

type NewServicoFormRawValue = FormValueOf<NewServico>;

type ServicoFormDefaults = Pick<NewServico, 'id' | 'startDate' | 'endDate'>;

type ServicoFormGroupContent = {
  id: FormControl<ServicoFormRawValue['id'] | NewServico['id']>;
  startDate: FormControl<ServicoFormRawValue['startDate']>;
  endDate: FormControl<ServicoFormRawValue['endDate']>;
  nomeDoServico: FormControl<ServicoFormRawValue['nomeDoServico']>;
  resumo: FormControl<ServicoFormRawValue['resumo']>;
};

export type ServicoFormGroup = FormGroup<ServicoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServicoFormService {
  createServicoFormGroup(servico: ServicoFormGroupInput = { id: null }): ServicoFormGroup {
    const servicoRawValue = this.convertServicoToServicoRawValue({
      ...this.getFormDefaults(),
      ...servico,
    });
    return new FormGroup<ServicoFormGroupContent>({
      id: new FormControl(
        { value: servicoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startDate: new FormControl(servicoRawValue.startDate),
      endDate: new FormControl(servicoRawValue.endDate),
      nomeDoServico: new FormControl(servicoRawValue.nomeDoServico),
      resumo: new FormControl(servicoRawValue.resumo),
    });
  }

  getServico(form: ServicoFormGroup): IServico | NewServico {
    return this.convertServicoRawValueToServico(form.getRawValue() as ServicoFormRawValue | NewServicoFormRawValue);
  }

  resetForm(form: ServicoFormGroup, servico: ServicoFormGroupInput): void {
    const servicoRawValue = this.convertServicoToServicoRawValue({ ...this.getFormDefaults(), ...servico });
    form.reset(
      {
        ...servicoRawValue,
        id: { value: servicoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServicoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertServicoRawValueToServico(rawServico: ServicoFormRawValue | NewServicoFormRawValue): IServico | NewServico {
    return {
      ...rawServico,
      startDate: dayjs(rawServico.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawServico.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertServicoToServicoRawValue(
    servico: IServico | (Partial<NewServico> & ServicoFormDefaults),
  ): ServicoFormRawValue | PartialWithRequiredKeyOf<NewServicoFormRawValue> {
    return {
      ...servico,
      startDate: servico.startDate ? servico.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: servico.endDate ? servico.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
