import { ILocal, NewLocal } from './local.model';

export const sampleWithRequiredData: ILocal = {
  id: 6123,
};

export const sampleWithPartialData: ILocal = {
  id: 20072,
};

export const sampleWithFullData: ILocal = {
  id: 27244,
  nomeDoLocal: 'below wrongly and',
};

export const sampleWithNewData: NewLocal = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
