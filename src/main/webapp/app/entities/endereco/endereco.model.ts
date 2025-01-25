import { IPais } from 'app/entities/pais/pais.model';

export interface IEndereco {
  id: number;
  rua?: string | null;
  cep?: string | null;
  cidade?: string | null;
  estado?: string | null;
  pais?: IPais | null;
}

export type NewEndereco = Omit<IEndereco, 'id'> & { id: null };
