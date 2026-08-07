export interface ITypeOfDocument {
  id: number;
  name?: string | null;
}

export type NewTypeOfDocument = Omit<ITypeOfDocument, 'id'> & { id: null };
