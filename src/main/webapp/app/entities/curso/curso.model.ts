import { IMateria } from 'app/entities/materia/materia.model';
import { IAluno } from 'app/entities/aluno/aluno.model';

export interface ICurso {
  id: number;
  nome?: string | null;
  materias?: IMateria[] | null;
  alunos?: IAluno[] | null;
}

export type NewCurso = Omit<ICurso, 'id'> & { id: null };
