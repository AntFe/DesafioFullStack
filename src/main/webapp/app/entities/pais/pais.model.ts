import { ILocal } from 'app/entities/local/local.model';

export interface IPais {
  id: number;
  nomeDoPais?: string | null;
  local?: ILocal | null;
}

export type NewPais = Omit<IPais, 'id'> & { id: null };
