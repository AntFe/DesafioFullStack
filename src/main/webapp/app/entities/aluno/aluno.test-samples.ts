import dayjs from 'dayjs/esm';

import { IAluno, NewAluno } from './aluno.model';

export const sampleWithRequiredData: IAluno = {
  id: 8806,
};

export const sampleWithPartialData: IAluno = {
  id: 25357,
  nome: 'husk randomize well-off',
  email: 'Ivan82@yahoo.com',
  numeroTelefone: 'aha',
};

export const sampleWithFullData: IAluno = {
  id: 25732,
  nome: 'oof near likewise',
  sobrenome: 'furiously',
  email: 'Hugo_Monteiro65@hotmail.com',
  numeroTelefone: 'incidentally dimly',
  matriculaData: dayjs('2025-01-24T06:10'),
  maticula: 'how circulate',
};

export const sampleWithNewData: NewAluno = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
