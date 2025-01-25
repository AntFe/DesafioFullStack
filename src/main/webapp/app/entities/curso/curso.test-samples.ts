import { ICurso, NewCurso } from './curso.model';

export const sampleWithRequiredData: ICurso = {
  id: 10742,
  nome: 'contrail present',
};

export const sampleWithPartialData: ICurso = {
  id: 24928,
  nome: 'through phew',
};

export const sampleWithFullData: ICurso = {
  id: 24432,
  nome: 'switch',
};

export const sampleWithNewData: NewCurso = {
  nome: 'equal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
