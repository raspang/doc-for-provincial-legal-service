import dayjs from 'dayjs/esm';

import { IDocumentReference, NewDocumentReference } from './document-reference.model';

export const sampleWithRequiredData: IDocumentReference = {
  id: 29543,
  date: dayjs('2026-08-06T18:38'),
  documentTitle: 'separately handy',
};

export const sampleWithPartialData: IDocumentReference = {
  id: 24161,
  date: dayjs('2026-08-06T21:38'),
  referenceNo: 'over pushy for',
  documentTitle: 'upliftingly',
  dateReleased: dayjs('2026-08-06T16:05'),
  submittedToSirKing: dayjs('2026-08-06T22:40'),
};

export const sampleWithFullData: IDocumentReference = {
  id: 24481,
  date: dayjs('2026-08-06T14:18'),
  referenceNo: 'pish above',
  documentTitle: 'needy',
  author: 'despite fatally fencing',
  dateReleased: dayjs('2026-08-07T05:51'),
  submittedToSirKing: dayjs('2026-08-07T06:33'),
  remarks: 'pitiful presume psst',
};

export const sampleWithNewData: NewDocumentReference = {
  date: dayjs('2026-08-06T22:06'),
  documentTitle: 'reword',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
