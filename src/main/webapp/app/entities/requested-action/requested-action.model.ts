export interface IRequestedAction {
  id: number;
  name?: string | null;
}

export type NewRequestedAction = Omit<IRequestedAction, 'id'> & { id: null };
