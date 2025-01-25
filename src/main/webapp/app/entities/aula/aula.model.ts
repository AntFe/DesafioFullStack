import { IMateria } from 'app/entities/materia/materia.model';

export interface IAula {
  id: number;
  tituloAula?: string | null;
  descricao?: string | null;
  linkVideo?: string | null;
  linkArquivos?: string | null;
  resumo?: string | null;
  materias?: IMateria[] | null;
}

export type NewAula = Omit<IAula, 'id'> & { id: null };
