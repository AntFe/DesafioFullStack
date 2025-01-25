import { IEndereco, NewEndereco } from './endereco.model';

export const sampleWithRequiredData: IEndereco = {
  id: 5176,
};

export const sampleWithPartialData: IEndereco = {
  id: 20088,
  estado: 'faint verbally',
};

export const sampleWithFullData: IEndereco = {
  id: 1847,
  rua: 'geez ugh',
  cep: 'plan whitewash electric',
  cidade: 'fooey psst furthermore',
  estado: 'um worth than',
};

export const sampleWithNewData: NewEndereco = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
