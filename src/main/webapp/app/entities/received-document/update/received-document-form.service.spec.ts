import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../received-document.test-samples';

import { ReceivedDocumentFormService } from './received-document-form.service';

describe('ReceivedDocument Form Service', () => {
  let service: ReceivedDocumentFormService;

  beforeEach(() => {
    service = TestBed.inject(ReceivedDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createReceivedDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReceivedDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            documentTitle: expect.any(Object),
            dateReleased: expect.any(Object),
            remarks: expect.any(Object),
            requestedAction: expect.any(Object),
            typeOfDocument: expect.any(Object),
            office: expect.any(Object),
            responsiblePerson: expect.any(Object),
            documentStatus: expect.any(Object),
            transactionType: expect.any(Object),
          }),
        );
      });

      it('passing IReceivedDocument should create a new form with FormGroup', () => {
        const formGroup = service.createReceivedDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            documentTitle: expect.any(Object),
            dateReleased: expect.any(Object),
            remarks: expect.any(Object),
            requestedAction: expect.any(Object),
            typeOfDocument: expect.any(Object),
            office: expect.any(Object),
            responsiblePerson: expect.any(Object),
            documentStatus: expect.any(Object),
            transactionType: expect.any(Object),
          }),
        );
      });
    });

    describe('getReceivedDocument', () => {
      it('should return NewReceivedDocument for default ReceivedDocument initial value', () => {
        const formGroup = service.createReceivedDocumentFormGroup(sampleWithNewData);

        const receivedDocument = service.getReceivedDocument(formGroup);

        expect(receivedDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewReceivedDocument for empty ReceivedDocument initial value', () => {
        const formGroup = service.createReceivedDocumentFormGroup();

        const receivedDocument = service.getReceivedDocument(formGroup);

        expect(receivedDocument).toMatchObject({});
      });

      it('should return IReceivedDocument', () => {
        const formGroup = service.createReceivedDocumentFormGroup(sampleWithRequiredData);

        const receivedDocument = service.getReceivedDocument(formGroup);

        expect(receivedDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReceivedDocument should not enable id FormControl', () => {
        const formGroup = service.createReceivedDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReceivedDocument should disable id FormControl', () => {
        const formGroup = service.createReceivedDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
