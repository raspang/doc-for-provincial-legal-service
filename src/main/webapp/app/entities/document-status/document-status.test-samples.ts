import { IDocumentStatus, NewDocumentStatus } from './document-status.model';

export const sampleWithRequiredData: IDocumentStatus = {
  id: 16534,
  name: 'underneath',
  color: 'purple',
};

export const sampleWithPartialData: IDocumentStatus = {
  id: 6233,
  name: 'runny aw reborn',
  color: 'ivory',
  warning: true,
};

export const sampleWithFullData: IDocumentStatus = {
  id: 5664,
  name: 'defiantly mothball',
  color: 'turquoise',
  warning: false,
};

export const sampleWithNewData: NewDocumentStatus = {
  name: 'arbitrate',
  color: 'fuchsia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
