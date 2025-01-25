import { IAula, NewAula } from './aula.model';

export const sampleWithRequiredData: IAula = {
  id: 25046,
};

export const sampleWithPartialData: IAula = {
  id: 8366,
  descricao: 'cruelly sonnet',
  linkVideo: 'snow nervously',
};

export const sampleWithFullData: IAula = {
  id: 20771,
  tituloAula: 'second-hand',
  descricao: 'sunder',
  linkVideo: 'neglect hence bah',
  linkArquivos: 'through pike',
  resumo: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAula = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
