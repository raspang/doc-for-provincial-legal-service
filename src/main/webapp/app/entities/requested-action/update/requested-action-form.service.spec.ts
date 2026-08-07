import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../requested-action.test-samples';

import { RequestedActionFormService } from './requested-action-form.service';

describe('RequestedAction Form Service', () => {
  let service: RequestedActionFormService;

  beforeEach(() => {
    service = TestBed.inject(RequestedActionFormService);
  });

  describe('Service methods', () => {
    describe('createRequestedActionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRequestedActionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IRequestedAction should create a new form with FormGroup', () => {
        const formGroup = service.createRequestedActionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getRequestedAction', () => {
      it('should return NewRequestedAction for default RequestedAction initial value', () => {
        const formGroup = service.createRequestedActionFormGroup(sampleWithNewData);

        const requestedAction = service.getRequestedAction(formGroup);

        expect(requestedAction).toMatchObject(sampleWithNewData);
      });

      it('should return NewRequestedAction for empty RequestedAction initial value', () => {
        const formGroup = service.createRequestedActionFormGroup();

        const requestedAction = service.getRequestedAction(formGroup);

        expect(requestedAction).toMatchObject({});
      });

      it('should return IRequestedAction', () => {
        const formGroup = service.createRequestedActionFormGroup(sampleWithRequiredData);

        const requestedAction = service.getRequestedAction(formGroup);

        expect(requestedAction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRequestedAction should not enable id FormControl', () => {
        const formGroup = service.createRequestedActionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRequestedAction should disable id FormControl', () => {
        const formGroup = service.createRequestedActionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
