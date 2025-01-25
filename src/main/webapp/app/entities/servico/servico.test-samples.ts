import dayjs from 'dayjs/esm';

import { IServico, NewServico } from './servico.model';

export const sampleWithRequiredData: IServico = {
  id: 10127,
};

export const sampleWithPartialData: IServico = {
  id: 23423,
  startDate: dayjs('2025-01-24T19:55'),
  endDate: dayjs('2025-01-24T21:14'),
  resumo: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IServico = {
  id: 17444,
  startDate: dayjs('2025-01-25T00:44'),
  endDate: dayjs('2025-01-24T18:02'),
  nomeDoServico: 'step-mother',
  resumo: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewServico = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
