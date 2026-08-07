import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRequestedAction, NewRequestedAction } from '../requested-action.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRequestedAction for edit and NewRequestedActionFormGroupInput for create.
 */
type RequestedActionFormGroupInput = IRequestedAction | PartialWithRequiredKeyOf<NewRequestedAction>;

type RequestedActionFormDefaults = Pick<NewRequestedAction, 'id'>;

type RequestedActionFormGroupContent = {
  id: FormControl<IRequestedAction['id'] | NewRequestedAction['id']>;
  name: FormControl<IRequestedAction['name']>;
};

export type RequestedActionFormGroup = FormGroup<RequestedActionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RequestedActionFormService {
  createRequestedActionFormGroup(requestedAction?: RequestedActionFormGroupInput): RequestedActionFormGroup {
    const requestedActionRawValue = {
      ...this.getFormDefaults(),
      ...(requestedAction ?? { id: null }),
    };

    return new FormGroup<RequestedActionFormGroupContent>({
      id: new FormControl(
        { value: requestedActionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(requestedActionRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getRequestedAction(form: RequestedActionFormGroup): IRequestedAction | NewRequestedAction {
    return form.getRawValue();
  }

  resetForm(form: RequestedActionFormGroup, requestedAction: RequestedActionFormGroupInput): void {
    const requestedActionRawValue = { ...this.getFormDefaults(), ...requestedAction };
    form.reset({
      ...requestedActionRawValue,
      id: { value: requestedActionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RequestedActionFormDefaults {
    return {
      id: null,
    };
  }
}
