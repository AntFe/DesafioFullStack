import dayjs from 'dayjs/esm';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { ICurso } from 'app/entities/curso/curso.model';

export interface IAluno {
  id: number;
  nome?: string | null;
  sobrenome?: string | null;
  email?: string | null;
  numeroTelefone?: string | null;
  matriculaData?: dayjs.Dayjs | null;
  maticula?: string | null;
  endereco?: IEndereco | null;
  cursos?: ICurso[] | null;
}

export type NewAluno = Omit<IAluno, 'id'> & { id: null };
