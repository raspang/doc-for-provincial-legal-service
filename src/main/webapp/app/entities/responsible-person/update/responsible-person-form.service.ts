import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IResponsiblePerson, NewResponsiblePerson } from '../responsible-person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResponsiblePerson for edit and NewResponsiblePersonFormGroupInput for create.
 */
type ResponsiblePersonFormGroupInput = IResponsiblePerson | PartialWithRequiredKeyOf<NewResponsiblePerson>;

type ResponsiblePersonFormDefaults = Pick<NewResponsiblePerson, 'id'>;

type ResponsiblePersonFormGroupContent = {
  id: FormControl<IResponsiblePerson['id'] | NewResponsiblePerson['id']>;
  name: FormControl<IResponsiblePerson['name']>;
  position: FormControl<IResponsiblePerson['position']>;
  email: FormControl<IResponsiblePerson['email']>;
  contactNo: FormControl<IResponsiblePerson['contactNo']>;
};

export type ResponsiblePersonFormGroup = FormGroup<ResponsiblePersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResponsiblePersonFormService {
  createResponsiblePersonFormGroup(responsiblePerson?: ResponsiblePersonFormGroupInput): ResponsiblePersonFormGroup {
    const responsiblePersonRawValue = {
      ...this.getFormDefaults(),
      ...(responsiblePerson ?? { id: null }),
    };

    return new FormGroup<ResponsiblePersonFormGroupContent>({
      id: new FormControl(
        { value: responsiblePersonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(responsiblePersonRawValue.name, {
        validators: [Validators.required],
      }),
      position: new FormControl(responsiblePersonRawValue.position),
      email: new FormControl(responsiblePersonRawValue.email, {
        validators: [Validators.required],
      }),
      contactNo: new FormControl(responsiblePersonRawValue.contactNo),
    });
  }

  getResponsiblePerson(form: ResponsiblePersonFormGroup): IResponsiblePerson | NewResponsiblePerson {
    return form.getRawValue();
  }

  resetForm(form: ResponsiblePersonFormGroup, responsiblePerson: ResponsiblePersonFormGroupInput): void {
    const responsiblePersonRawValue = { ...this.getFormDefaults(), ...responsiblePerson };
    form.reset({
      ...responsiblePersonRawValue,
      id: { value: responsiblePersonRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ResponsiblePersonFormDefaults {
    return {
      id: null,
    };
  }
}
