import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-status.test-samples';

import { DocumentStatusFormService } from './document-status-form.service';

describe('DocumentStatus Form Service', () => {
  let service: DocumentStatusFormService;

  beforeEach(() => {
    service = TestBed.inject(DocumentStatusFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            color: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentStatus should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            color: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentStatus', () => {
      it('should return NewDocumentStatus for default DocumentStatus initial value', () => {
        const formGroup = service.createDocumentStatusFormGroup(sampleWithNewData);

        const documentStatus = service.getDocumentStatus(formGroup);

        expect(documentStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentStatus for empty DocumentStatus initial value', () => {
        const formGroup = service.createDocumentStatusFormGroup();

        const documentStatus = service.getDocumentStatus(formGroup);

        expect(documentStatus).toMatchObject({});
      });

      it('should return IDocumentStatus', () => {
        const formGroup = service.createDocumentStatusFormGroup(sampleWithRequiredData);

        const documentStatus = service.getDocumentStatus(formGroup);

        expect(documentStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentStatus should not enable id FormControl', () => {
        const formGroup = service.createDocumentStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentStatus should disable id FormControl', () => {
        const formGroup = service.createDocumentStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
