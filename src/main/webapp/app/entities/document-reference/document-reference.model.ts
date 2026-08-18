import dayjs from 'dayjs/esm';

import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';

export interface IDocumentReference {
  id: number;
  date?: dayjs.Dayjs | null;
  referenceNo?: string | null;
  documentTitle?: string | null;
  author?: string | null;
  dateReleased?: dayjs.Dayjs | null;
  submittedToSirKing?: dayjs.Dayjs | null;
  remarks?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  typeOfDocument?: Pick<ITypeOfDocument, 'id' | 'name'> | null;
}

export type NewDocumentReference = Omit<IDocumentReference, 'id'> & { id: null };
