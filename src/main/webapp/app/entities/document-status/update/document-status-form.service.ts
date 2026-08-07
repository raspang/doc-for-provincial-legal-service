import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDocumentStatus, NewDocumentStatus } from '../document-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentStatus for edit and NewDocumentStatusFormGroupInput for create.
 */
type DocumentStatusFormGroupInput = IDocumentStatus | PartialWithRequiredKeyOf<NewDocumentStatus>;

type DocumentStatusFormDefaults = Pick<NewDocumentStatus, 'id'>;

type DocumentStatusFormGroupContent = {
  id: FormControl<IDocumentStatus['id'] | NewDocumentStatus['id']>;
  name: FormControl<IDocumentStatus['name']>;
  color: FormControl<IDocumentStatus['color']>;
};

export type DocumentStatusFormGroup = FormGroup<DocumentStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentStatusFormService {
  createDocumentStatusFormGroup(documentStatus?: DocumentStatusFormGroupInput): DocumentStatusFormGroup {
    const documentStatusRawValue = {
      ...this.getFormDefaults(),
      ...(documentStatus ?? { id: null }),
    };

    return new FormGroup<DocumentStatusFormGroupContent>({
      id: new FormControl(
        { value: documentStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(documentStatusRawValue.name, {
        validators: [Validators.required],
      }),
      color: new FormControl(documentStatusRawValue.color, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentStatus(form: DocumentStatusFormGroup): IDocumentStatus | NewDocumentStatus {
    return form.getRawValue();
  }

  resetForm(form: DocumentStatusFormGroup, documentStatus: DocumentStatusFormGroupInput): void {
    const documentStatusRawValue = { ...this.getFormDefaults(), ...documentStatus };
    form.reset({
      ...documentStatusRawValue,
      id: { value: documentStatusRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentStatusFormDefaults {
    return {
      id: null,
    };
  }
}
