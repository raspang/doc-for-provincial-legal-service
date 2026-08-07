import dayjs from 'dayjs/esm';

import { IReceivedDocument, NewReceivedDocument } from './received-document.model';

export const sampleWithRequiredData: IReceivedDocument = {
  id: 4296,
  date: dayjs('2026-08-07T00:50'),
  documentTitle: 'pop',
};

export const sampleWithPartialData: IReceivedDocument = {
  id: 11254,
  date: dayjs('2026-08-06T22:48'),
  documentTitle: 'scaffold',
  days: 16251,
  dueDate: dayjs('2026-08-06T18:05'),
  daysBeforeDue: 22757,
};

export const sampleWithFullData: IReceivedDocument = {
  id: 32056,
  date: dayjs('2026-08-06T23:10'),
  documentTitle: 'gummy taut',
  transactionType: 'NOT_APPLICABLE',
  days: 6225,
  dueDate: dayjs('2026-08-07T06:31'),
  daysBeforeDue: 12941,
  dateReleased: dayjs('2026-08-06T16:47'),
  remarks: 'nutritious mystify',
};

export const sampleWithNewData: NewReceivedDocument = {
  date: dayjs('2026-08-07T07:04'),
  documentTitle: 'depend pfft congregate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
