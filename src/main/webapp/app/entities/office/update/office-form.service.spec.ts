import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../office.test-samples';

import { OfficeFormService } from './office-form.service';

describe('Office Form Service', () => {
  let service: OfficeFormService;

  beforeEach(() => {
    service = TestBed.inject(OfficeFormService);
  });

  describe('Service methods', () => {
    describe('createOfficeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOfficeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            shortName: expect.any(Object),
          }),
        );
      });

      it('passing IOffice should create a new form with FormGroup', () => {
        const formGroup = service.createOfficeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            shortName: expect.any(Object),
          }),
        );
      });
    });

    describe('getOffice', () => {
      it('should return NewOffice for default Office initial value', () => {
        const formGroup = service.createOfficeFormGroup(sampleWithNewData);

        const office = service.getOffice(formGroup);

        expect(office).toMatchObject(sampleWithNewData);
      });

      it('should return NewOffice for empty Office initial value', () => {
        const formGroup = service.createOfficeFormGroup();

        const office = service.getOffice(formGroup);

        expect(office).toMatchObject({});
      });

      it('should return IOffice', () => {
        const formGroup = service.createOfficeFormGroup(sampleWithRequiredData);

        const office = service.getOffice(formGroup);

        expect(office).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOffice should not enable id FormControl', () => {
        const formGroup = service.createOfficeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOffice should disable id FormControl', () => {
        const formGroup = service.createOfficeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
