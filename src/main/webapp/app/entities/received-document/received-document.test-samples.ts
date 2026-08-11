import dayjs from 'dayjs/esm';

import { IReceivedDocument, NewReceivedDocument } from './received-document.model';

export const sampleWithRequiredData: IReceivedDocument = {
  id: 4296,
  date: dayjs('2026-08-07T00:50'),
  documentTitle: 'pop',
};

export const sampleWithPartialData: IReceivedDocument = {
  id: 5705,
  date: dayjs('2026-08-06T08:21'),
  documentTitle: 'competent populist',
  remarks: 'common in',
};

export const sampleWithFullData: IReceivedDocument = {
  id: 32056,
  date: dayjs('2026-08-06T23:10'),
  documentTitle: 'gummy taut',
  dateReleased: dayjs('2026-08-07T04:33'),
  remarks: 'intelligent',
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
