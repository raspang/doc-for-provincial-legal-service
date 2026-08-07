import { IResponsiblePerson, NewResponsiblePerson } from './responsible-person.model';

export const sampleWithRequiredData: IResponsiblePerson = {
  id: 16325,
  name: 'department',
  email: 'Rosalind.Howell81@gmail.com',
};

export const sampleWithPartialData: IResponsiblePerson = {
  id: 21950,
  name: 'aw',
  position: 'eek freely circumference',
  email: 'Janelle_Keebler86@yahoo.com',
  contactNo: 'jubilantly bleak through',
};

export const sampleWithFullData: IResponsiblePerson = {
  id: 9517,
  name: 'against without usefully',
  position: 'duh',
  email: 'Jimmie_Hermiston@yahoo.com',
  contactNo: 'when',
};

export const sampleWithNewData: NewResponsiblePerson = {
  name: 'gadzooks gosh oddly',
  email: 'Charlie_OKon@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
