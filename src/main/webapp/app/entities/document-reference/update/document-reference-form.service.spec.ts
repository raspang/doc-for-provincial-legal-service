import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-reference.test-samples';

import { DocumentReferenceFormService } from './document-reference-form.service';

describe('DocumentReference Form Service', () => {
  let service: DocumentReferenceFormService;

  beforeEach(() => {
    service = TestBed.inject(DocumentReferenceFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentReferenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentReferenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            referenceNo: expect.any(Object),
            documentTitle: expect.any(Object),
            author: expect.any(Object),
            dateReleased: expect.any(Object),
            submittedToSirKing: expect.any(Object),
            remarks: expect.any(Object),
            typeOfDocument: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentReference should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentReferenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            referenceNo: expect.any(Object),
            documentTitle: expect.any(Object),
            author: expect.any(Object),
            dateReleased: expect.any(Object),
            submittedToSirKing: expect.any(Object),
            remarks: expect.any(Object),
            typeOfDocument: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentReference', () => {
      it('should return NewDocumentReference for default DocumentReference initial value', () => {
        const formGroup = service.createDocumentReferenceFormGroup(sampleWithNewData);

        const documentReference = service.getDocumentReference(formGroup);

        expect(documentReference).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentReference for empty DocumentReference initial value', () => {
        const formGroup = service.createDocumentReferenceFormGroup();

        const documentReference = service.getDocumentReference(formGroup);

        expect(documentReference).toMatchObject({});
      });

      it('should return IDocumentReference', () => {
        const formGroup = service.createDocumentReferenceFormGroup(sampleWithRequiredData);

        const documentReference = service.getDocumentReference(formGroup);

        expect(documentReference).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentReference should not enable id FormControl', () => {
        const formGroup = service.createDocumentReferenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentReference should disable id FormControl', () => {
        const formGroup = service.createDocumentReferenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
