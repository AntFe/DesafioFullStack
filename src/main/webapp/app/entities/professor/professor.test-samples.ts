import dayjs from 'dayjs/esm';

import { IProfessor, NewProfessor } from './professor.model';

export const sampleWithRequiredData: IProfessor = {
  id: 2741,
};

export const sampleWithPartialData: IProfessor = {
  id: 11962,
  sobrenome: 'now wasabi',
  email: 'Feliciano_Lima81@yahoo.com',
  ingresso: dayjs('2025-01-24T17:40'),
  materiaLecionada: 'that gadzooks',
  registroProfissional: 'including',
};

export const sampleWithFullData: IProfessor = {
  id: 313,
  nome: 'excepting',
  sobrenome: 'phooey',
  email: 'Vania_Teixeira89@gmail.com',
  numeroTelefone: 'where',
  ingresso: dayjs('2025-01-25T04:57'),
  materiaLecionada: 'wealthy',
  registroProfissional: 'yum scale duh',
};

export const sampleWithNewData: NewProfessor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
