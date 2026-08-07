import dayjs from 'dayjs/esm';

import { IDocumentHistory } from './document-history.model';

export const sampleWithRequiredData: IDocumentHistory = {
  id: 31468,
  documentId: 12861,
  documentType: 'DOCUMENT_REFERENCE',
  action: 'octave chapel',
  changedBy: 'unlike',
  timestamp: dayjs('2026-08-06T20:08'),
};

export const sampleWithPartialData: IDocumentHistory = {
  id: 5049,
  documentId: 24028,
  documentType: 'DOCUMENT_REFERENCE',
  action: 'likewise until',
  changedBy: 'offset',
  timestamp: dayjs('2026-08-06T12:19'),
  newValue: 'wisely solicit',
  remarks: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IDocumentHistory = {
  id: 8870,
  documentId: 3558,
  documentType: 'DOCUMENT_REFERENCE',
  action: 'hourly phooey',
  changedBy: 'SUV',
  timestamp: dayjs('2026-08-06T11:54'),
  previousValue: 'phew while testify',
  newValue: 'whenever',
  remarks: '../fake-data/blob/hipster.txt',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
