export interface ILocal {
  id: number;
  nomeDoLocal?: string | null;
}

export type NewLocal = Omit<ILocal, 'id'> & { id: null };
