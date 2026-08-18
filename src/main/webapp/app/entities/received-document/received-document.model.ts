import dayjs from 'dayjs/esm';

import { IDocumentStatus } from 'app/entities/document-status/document-status.model';
import { IOffice } from 'app/entities/office/office.model';
import { IRequestedAction } from 'app/entities/requested-action/requested-action.model';
import { IResponsiblePerson } from 'app/entities/responsible-person/responsible-person.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';

export interface IReceivedDocument {
  id: number;
  date?: dayjs.Dayjs | null;
  documentTitle?: string | null;
  dateReleased?: dayjs.Dayjs | null;
  remarks?: string | null;
  requestedAction?: Pick<IRequestedAction, 'id' | 'name'> | null;
  typeOfDocument?: Pick<ITypeOfDocument, 'id' | 'name'> | null;
  office?: Pick<IOffice, 'id' | 'name'> | null;
  responsiblePerson?: Pick<IResponsiblePerson, 'id' | 'name'> | null;
  documentStatus?: Pick<IDocumentStatus, 'id' | 'name' | 'color' | 'warning'> | null;
  transactionType?: Pick<ITransactionType, 'id' | 'name' | 'targetDays'> | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewReceivedDocument = Omit<IReceivedDocument, 'id'> & { id: null };
