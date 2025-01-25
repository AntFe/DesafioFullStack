import { IAula } from 'app/entities/aula/aula.model';
import { IProfessor } from 'app/entities/professor/professor.model';
import { ICurso } from 'app/entities/curso/curso.model';

export interface IMateria {
  id: number;
  nomeDaMateria?: string | null;
  ementa?: string | null;
  referenciasBibliograficas?: string | null;
  aulas?: IAula[] | null;
  professor?: IProfessor | null;
  cursos?: ICurso[] | null;
}

export type NewMateria = Omit<IMateria, 'id'> & { id: null };
