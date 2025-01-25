import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProfessor, NewProfessor } from '../professor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfessor for edit and NewProfessorFormGroupInput for create.
 */
type ProfessorFormGroupInput = IProfessor | PartialWithRequiredKeyOf<NewProfessor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProfessor | NewProfessor> = Omit<T, 'ingresso'> & {
  ingresso?: string | null;
};

type ProfessorFormRawValue = FormValueOf<IProfessor>;

type NewProfessorFormRawValue = FormValueOf<NewProfessor>;

type ProfessorFormDefaults = Pick<NewProfessor, 'id' | 'ingresso'>;

type ProfessorFormGroupContent = {
  id: FormControl<ProfessorFormRawValue['id'] | NewProfessor['id']>;
  nome: FormControl<ProfessorFormRawValue['nome']>;
  sobrenome: FormControl<ProfessorFormRawValue['sobrenome']>;
  email: FormControl<ProfessorFormRawValue['email']>;
  numeroTelefone: FormControl<ProfessorFormRawValue['numeroTelefone']>;
  ingresso: FormControl<ProfessorFormRawValue['ingresso']>;
  materiaLecionada: FormControl<ProfessorFormRawValue['materiaLecionada']>;
  registroProfissional: FormControl<ProfessorFormRawValue['registroProfissional']>;
  servico: FormControl<ProfessorFormRawValue['servico']>;
};

export type ProfessorFormGroup = FormGroup<ProfessorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfessorFormService {
  createProfessorFormGroup(professor: ProfessorFormGroupInput = { id: null }): ProfessorFormGroup {
    const professorRawValue = this.convertProfessorToProfessorRawValue({
      ...this.getFormDefaults(),
      ...professor,
    });
    return new FormGroup<ProfessorFormGroupContent>({
      id: new FormControl(
        { value: professorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(professorRawValue.nome),
      sobrenome: new FormControl(professorRawValue.sobrenome),
      email: new FormControl(professorRawValue.email),
      numeroTelefone: new FormControl(professorRawValue.numeroTelefone),
      ingresso: new FormControl(professorRawValue.ingresso),
      materiaLecionada: new FormControl(professorRawValue.materiaLecionada),
      registroProfissional: new FormControl(professorRawValue.registroProfissional),
      servico: new FormControl(professorRawValue.servico),
    });
  }

  getProfessor(form: ProfessorFormGroup): IProfessor | NewProfessor {
    return this.convertProfessorRawValueToProfessor(form.getRawValue() as ProfessorFormRawValue | NewProfessorFormRawValue);
  }

  resetForm(form: ProfessorFormGroup, professor: ProfessorFormGroupInput): void {
    const professorRawValue = this.convertProfessorToProfessorRawValue({ ...this.getFormDefaults(), ...professor });
    form.reset(
      {
        ...professorRawValue,
        id: { value: professorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProfessorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      ingresso: currentTime,
    };
  }

  private convertProfessorRawValueToProfessor(rawProfessor: ProfessorFormRawValue | NewProfessorFormRawValue): IProfessor | NewProfessor {
    return {
      ...rawProfessor,
      ingresso: dayjs(rawProfessor.ingresso, DATE_TIME_FORMAT),
    };
  }

  private convertProfessorToProfessorRawValue(
    professor: IProfessor | (Partial<NewProfessor> & ProfessorFormDefaults),
  ): ProfessorFormRawValue | PartialWithRequiredKeyOf<NewProfessorFormRawValue> {
    return {
      ...professor,
      ingresso: professor.ingresso ? professor.ingresso.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
