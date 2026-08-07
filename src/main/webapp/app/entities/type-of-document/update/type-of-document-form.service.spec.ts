import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../type-of-document.test-samples';

import { TypeOfDocumentFormService } from './type-of-document-form.service';

describe('TypeOfDocument Form Service', () => {
  let service: TypeOfDocumentFormService;

  beforeEach(() => {
    service = TestBed.inject(TypeOfDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createTypeOfDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeOfDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing ITypeOfDocument should create a new form with FormGroup', () => {
        const formGroup = service.createTypeOfDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeOfDocument', () => {
      it('should return NewTypeOfDocument for default TypeOfDocument initial value', () => {
        const formGroup = service.createTypeOfDocumentFormGroup(sampleWithNewData);

        const typeOfDocument = service.getTypeOfDocument(formGroup);

        expect(typeOfDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeOfDocument for empty TypeOfDocument initial value', () => {
        const formGroup = service.createTypeOfDocumentFormGroup();

        const typeOfDocument = service.getTypeOfDocument(formGroup);

        expect(typeOfDocument).toMatchObject({});
      });

      it('should return ITypeOfDocument', () => {
        const formGroup = service.createTypeOfDocumentFormGroup(sampleWithRequiredData);

        const typeOfDocument = service.getTypeOfDocument(formGroup);

        expect(typeOfDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeOfDocument should not enable id FormControl', () => {
        const formGroup = service.createTypeOfDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeOfDocument should disable id FormControl', () => {
        const formGroup = service.createTypeOfDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
