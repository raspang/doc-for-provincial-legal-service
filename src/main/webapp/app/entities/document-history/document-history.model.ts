import dayjs from 'dayjs/esm';

import { DocumentType } from 'app/entities/enumerations/document-type.model';

export interface IDocumentHistory {
  id: number;
  documentId?: number | null;
  documentType?: keyof typeof DocumentType | null;
  action?: string | null;
  changedBy?: string | null;
  timestamp?: dayjs.Dayjs | null;
  previousValue?: string | null;
  newValue?: string | null;
  remarks?: string | null;
}
