export interface ITransactionType {
  id: number;
  name?: string | null;
  targetDays?: number | null;
}

export type NewTransactionType = Omit<ITransactionType, 'id'> & { id: null };
