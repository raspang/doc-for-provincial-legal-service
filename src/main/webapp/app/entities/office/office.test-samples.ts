import { IOffice, NewOffice } from './office.model';

export const sampleWithRequiredData: IOffice = {
  id: 5878,
  name: 'ring aboard finer',
};

export const sampleWithPartialData: IOffice = {
  id: 12102,
  name: 'plus reconstitute worth',
};

export const sampleWithFullData: IOffice = {
  id: 31331,
  name: 'absolve quirkily',
  shortName: 'scent',
};

export const sampleWithNewData: NewOffice = {
  name: 'license',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
