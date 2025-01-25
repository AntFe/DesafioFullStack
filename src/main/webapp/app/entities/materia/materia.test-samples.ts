import { IMateria, NewMateria } from './materia.model';

export const sampleWithRequiredData: IMateria = {
  id: 6992,
};

export const sampleWithPartialData: IMateria = {
  id: 26298,
  nomeDaMateria: 'fully',
  ementa: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IMateria = {
  id: 28944,
  nomeDaMateria: 'enormously onto hoof',
  ementa: '../fake-data/blob/hipster.txt',
  referenciasBibliograficas: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewMateria = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
