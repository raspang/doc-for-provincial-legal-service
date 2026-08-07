export interface IDocumentStatus {
  id: number;
  name?: string | null;
  color?: string | null;
}

export type NewDocumentStatus = Omit<IDocumentStatus, 'id'> & { id: null };
