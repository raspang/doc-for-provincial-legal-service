import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITransactionType, NewTransactionType } from '../transaction-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionType for edit and NewTransactionTypeFormGroupInput for create.
 */
type TransactionTypeFormGroupInput = ITransactionType | PartialWithRequiredKeyOf<NewTransactionType>;

type TransactionTypeFormDefaults = Pick<NewTransactionType, 'id'>;

type TransactionTypeFormGroupContent = {
  id: FormControl<ITransactionType['id'] | NewTransactionType['id']>;
  name: FormControl<ITransactionType['name']>;
  targetDays: FormControl<ITransactionType['targetDays']>;
};

export type TransactionTypeFormGroup = FormGroup<TransactionTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionTypeFormService {
  createTransactionTypeFormGroup(transactionType?: TransactionTypeFormGroupInput): TransactionTypeFormGroup {
    const transactionTypeRawValue = {
      ...this.getFormDefaults(),
      ...(transactionType ?? { id: null }),
    };

    return new FormGroup<TransactionTypeFormGroupContent>({
      id: new FormControl(
        { value: transactionTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(transactionTypeRawValue.name, {
        validators: [Validators.required],
      }),
      targetDays: new FormControl(transactionTypeRawValue.targetDays),
    });
  }

  getTransactionType(form: TransactionTypeFormGroup): ITransactionType | NewTransactionType {
    return form.getRawValue();
  }

  resetForm(form: TransactionTypeFormGroup, transactionType: TransactionTypeFormGroupInput): void {
    const transactionTypeRawValue = { ...this.getFormDefaults(), ...transactionType };
    form.reset({
      ...transactionTypeRawValue,
      id: { value: transactionTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TransactionTypeFormDefaults {
    return {
      id: null,
    };
  }
}
