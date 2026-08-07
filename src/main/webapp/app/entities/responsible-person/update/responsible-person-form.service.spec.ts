import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../responsible-person.test-samples';

import { ResponsiblePersonFormService } from './responsible-person-form.service';

describe('ResponsiblePerson Form Service', () => {
  let service: ResponsiblePersonFormService;

  beforeEach(() => {
    service = TestBed.inject(ResponsiblePersonFormService);
  });

  describe('Service methods', () => {
    describe('createResponsiblePersonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResponsiblePersonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            position: expect.any(Object),
            email: expect.any(Object),
            contactNo: expect.any(Object),
          }),
        );
      });

      it('passing IResponsiblePerson should create a new form with FormGroup', () => {
        const formGroup = service.createResponsiblePersonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            position: expect.any(Object),
            email: expect.any(Object),
            contactNo: expect.any(Object),
          }),
        );
      });
    });

    describe('getResponsiblePerson', () => {
      it('should return NewResponsiblePerson for default ResponsiblePerson initial value', () => {
        const formGroup = service.createResponsiblePersonFormGroup(sampleWithNewData);

        const responsiblePerson = service.getResponsiblePerson(formGroup);

        expect(responsiblePerson).toMatchObject(sampleWithNewData);
      });

      it('should return NewResponsiblePerson for empty ResponsiblePerson initial value', () => {
        const formGroup = service.createResponsiblePersonFormGroup();

        const responsiblePerson = service.getResponsiblePerson(formGroup);

        expect(responsiblePerson).toMatchObject({});
      });

      it('should return IResponsiblePerson', () => {
        const formGroup = service.createResponsiblePersonFormGroup(sampleWithRequiredData);

        const responsiblePerson = service.getResponsiblePerson(formGroup);

        expect(responsiblePerson).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResponsiblePerson should not enable id FormControl', () => {
        const formGroup = service.createResponsiblePersonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResponsiblePerson should disable id FormControl', () => {
        const formGroup = service.createResponsiblePersonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
