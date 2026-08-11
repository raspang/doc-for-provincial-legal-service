export interface IDocumentStatus {
  id: number;
  name?: string | null;
  color?: string | null;
  warning?: boolean | null;
}

export type NewDocumentStatus = Omit<IDocumentStatus, 'id'> & { id: null };
