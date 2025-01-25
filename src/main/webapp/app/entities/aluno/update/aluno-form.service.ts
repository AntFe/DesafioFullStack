import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAluno, NewAluno } from '../aluno.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAluno for edit and NewAlunoFormGroupInput for create.
 */
type AlunoFormGroupInput = IAluno | PartialWithRequiredKeyOf<NewAluno>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAluno | NewAluno> = Omit<T, 'matriculaData'> & {
  matriculaData?: string | null;
};

type AlunoFormRawValue = FormValueOf<IAluno>;

type NewAlunoFormRawValue = FormValueOf<NewAluno>;

type AlunoFormDefaults = Pick<NewAluno, 'id' | 'matriculaData' | 'cursos'>;

type AlunoFormGroupContent = {
  id: FormControl<AlunoFormRawValue['id'] | NewAluno['id']>;
  nome: FormControl<AlunoFormRawValue['nome']>;
  sobrenome: FormControl<AlunoFormRawValue['sobrenome']>;
  email: FormControl<AlunoFormRawValue['email']>;
  numeroTelefone: FormControl<AlunoFormRawValue['numeroTelefone']>;
  matriculaData: FormControl<AlunoFormRawValue['matriculaData']>;
  maticula: FormControl<AlunoFormRawValue['maticula']>;
  endereco: FormControl<AlunoFormRawValue['endereco']>;
  cursos: FormControl<AlunoFormRawValue['cursos']>;
};

export type AlunoFormGroup = FormGroup<AlunoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlunoFormService {
  createAlunoFormGroup(aluno: AlunoFormGroupInput = { id: null }): AlunoFormGroup {
    const alunoRawValue = this.convertAlunoToAlunoRawValue({
      ...this.getFormDefaults(),
      ...aluno,
    });
    return new FormGroup<AlunoFormGroupContent>({
      id: new FormControl(
        { value: alunoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(alunoRawValue.nome),
      sobrenome: new FormControl(alunoRawValue.sobrenome),
      email: new FormControl(alunoRawValue.email),
      numeroTelefone: new FormControl(alunoRawValue.numeroTelefone),
      matriculaData: new FormControl(alunoRawValue.matriculaData),
      maticula: new FormControl(alunoRawValue.maticula),
      endereco: new FormControl(alunoRawValue.endereco),
      cursos: new FormControl(alunoRawValue.cursos ?? []),
    });
  }

  getAluno(form: AlunoFormGroup): IAluno | NewAluno {
    return this.convertAlunoRawValueToAluno(form.getRawValue() as AlunoFormRawValue | NewAlunoFormRawValue);
  }

  resetForm(form: AlunoFormGroup, aluno: AlunoFormGroupInput): void {
    const alunoRawValue = this.convertAlunoToAlunoRawValue({ ...this.getFormDefaults(), ...aluno });
    form.reset(
      {
        ...alunoRawValue,
        id: { value: alunoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlunoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      matriculaData: currentTime,
      cursos: [],
    };
  }

  private convertAlunoRawValueToAluno(rawAluno: AlunoFormRawValue | NewAlunoFormRawValue): IAluno | NewAluno {
    return {
      ...rawAluno,
      matriculaData: dayjs(rawAluno.matriculaData, DATE_TIME_FORMAT),
    };
  }

  private convertAlunoToAlunoRawValue(
    aluno: IAluno | (Partial<NewAluno> & AlunoFormDefaults),
  ): AlunoFormRawValue | PartialWithRequiredKeyOf<NewAlunoFormRawValue> {
    return {
      ...aluno,
      matriculaData: aluno.matriculaData ? aluno.matriculaData.format(DATE_TIME_FORMAT) : undefined,
      cursos: aluno.cursos ?? [],
    };
  }
}
