export interface IResponsiblePerson {
  id: number;
  name?: string | null;
  position?: string | null;
  email?: string | null;
  contactNo?: string | null;
}

export type NewResponsiblePerson = Omit<IResponsiblePerson, 'id'> & { id: null };
