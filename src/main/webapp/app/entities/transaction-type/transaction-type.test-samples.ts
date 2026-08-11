import { ITransactionType, NewTransactionType } from './transaction-type.model';

export const sampleWithRequiredData: ITransactionType = {
  id: 26122,
  name: 'drat phooey along',
};

export const sampleWithPartialData: ITransactionType = {
  id: 6113,
  name: 'presume arrogantly',
};

export const sampleWithFullData: ITransactionType = {
  id: 14350,
  name: 'openly',
  targetDays: 9251,
};

export const sampleWithNewData: NewTransactionType = {
  name: 'blah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
