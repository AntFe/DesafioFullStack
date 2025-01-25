import { IPais, NewPais } from './pais.model';

export const sampleWithRequiredData: IPais = {
  id: 1578,
};

export const sampleWithPartialData: IPais = {
  id: 3125,
  nomeDoPais: 'jubilantly',
};

export const sampleWithFullData: IPais = {
  id: 26897,
  nomeDoPais: 'mom nor',
};

export const sampleWithNewData: NewPais = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
