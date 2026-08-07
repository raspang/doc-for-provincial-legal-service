import { ITypeOfDocument, NewTypeOfDocument } from './type-of-document.model';

export const sampleWithRequiredData: ITypeOfDocument = {
  id: 330,
  name: 'although',
};

export const sampleWithPartialData: ITypeOfDocument = {
  id: 8964,
  name: 'light cappelletti',
};

export const sampleWithFullData: ITypeOfDocument = {
  id: 15118,
  name: 'or so ha',
};

export const sampleWithNewData: NewTypeOfDocument = {
  name: 'valuable despite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
