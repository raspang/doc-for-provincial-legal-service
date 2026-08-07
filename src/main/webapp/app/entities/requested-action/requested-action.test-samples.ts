import { IRequestedAction, NewRequestedAction } from './requested-action.model';

export const sampleWithRequiredData: IRequestedAction = {
  id: 23288,
  name: 'dividend orientate unless',
};

export const sampleWithPartialData: IRequestedAction = {
  id: 2084,
  name: 'circle defensive',
};

export const sampleWithFullData: IRequestedAction = {
  id: 20979,
  name: 'bobble freely',
};

export const sampleWithNewData: NewRequestedAction = {
  name: 'an inasmuch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
