export interface IOffice {
  id: number;
  name?: string | null;
  shortName?: string | null;
}

export type NewOffice = Omit<IOffice, 'id'> & { id: null };
