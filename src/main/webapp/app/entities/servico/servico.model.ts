import dayjs from 'dayjs/esm';

export interface IServico {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  nomeDoServico?: string | null;
  resumo?: string | null;
}

export type NewServico = Omit<IServico, 'id'> & { id: null };
