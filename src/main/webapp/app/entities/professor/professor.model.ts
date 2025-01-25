import dayjs from 'dayjs/esm';
import { IServico } from 'app/entities/servico/servico.model';

export interface IProfessor {
  id: number;
  nome?: string | null;
  sobrenome?: string | null;
  email?: string | null;
  numeroTelefone?: string | null;
  ingresso?: dayjs.Dayjs | null;
  materiaLecionada?: string | null;
  registroProfissional?: string | null;
  servico?: IServico | null;
}

export type NewProfessor = Omit<IProfessor, 'id'> & { id: null };
