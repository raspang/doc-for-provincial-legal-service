import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOffice, NewOffice } from '../office.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOffice for edit and NewOfficeFormGroupInput for create.
 */
type OfficeFormGroupInput = IOffice | PartialWithRequiredKeyOf<NewOffice>;

type OfficeFormDefaults = Pick<NewOffice, 'id'>;

type OfficeFormGroupContent = {
  id: FormControl<IOffice['id'] | NewOffice['id']>;
  name: FormControl<IOffice['name']>;
  shortName: FormControl<IOffice['shortName']>;
};

export type OfficeFormGroup = FormGroup<OfficeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OfficeFormService {
  createOfficeFormGroup(office?: OfficeFormGroupInput): OfficeFormGroup {
    const officeRawValue = {
      ...this.getFormDefaults(),
      ...(office ?? { id: null }),
    };

    return new FormGroup<OfficeFormGroupContent>({
      id: new FormControl(
        { value: officeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(officeRawValue.name, {
        validators: [Validators.required],
      }),
      shortName: new FormControl(officeRawValue.shortName),
    });
  }

  getOffice(form: OfficeFormGroup): IOffice | NewOffice {
    return form.getRawValue();
  }

  resetForm(form: OfficeFormGroup, office: OfficeFormGroupInput): void {
    const officeRawValue = { ...this.getFormDefaults(), ...office };
    form.reset({
      ...officeRawValue,
      id: { value: officeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OfficeFormDefaults {
    return {
      id: null,
    };
  }
}
