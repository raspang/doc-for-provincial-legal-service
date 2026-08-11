import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transaction-type.test-samples';

import { TransactionTypeFormService } from './transaction-type-form.service';

describe('TransactionType Form Service', () => {
  let service: TransactionTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(TransactionTypeFormService);
  });

  describe('Service methods', () => {
    describe('createTransactionTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransactionTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            targetDays: expect.any(Object),
          }),
        );
      });

      it('passing ITransactionType should create a new form with FormGroup', () => {
        const formGroup = service.createTransactionTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            targetDays: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransactionType', () => {
      it('should return NewTransactionType for default TransactionType initial value', () => {
        const formGroup = service.createTransactionTypeFormGroup(sampleWithNewData);

        const transactionType = service.getTransactionType(formGroup);

        expect(transactionType).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransactionType for empty TransactionType initial value', () => {
        const formGroup = service.createTransactionTypeFormGroup();

        const transactionType = service.getTransactionType(formGroup);

        expect(transactionType).toMatchObject({});
      });

      it('should return ITransactionType', () => {
        const formGroup = service.createTransactionTypeFormGroup(sampleWithRequiredData);

        const transactionType = service.getTransactionType(formGroup);

        expect(transactionType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransactionType should not enable id FormControl', () => {
        const formGroup = service.createTransactionTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransactionType should disable id FormControl', () => {
        const formGroup = service.createTransactionTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
