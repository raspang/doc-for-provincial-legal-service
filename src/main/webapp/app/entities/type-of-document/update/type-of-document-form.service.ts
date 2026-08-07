import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITypeOfDocument, NewTypeOfDocument } from '../type-of-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeOfDocument for edit and NewTypeOfDocumentFormGroupInput for create.
 */
type TypeOfDocumentFormGroupInput = ITypeOfDocument | PartialWithRequiredKeyOf<NewTypeOfDocument>;

type TypeOfDocumentFormDefaults = Pick<NewTypeOfDocument, 'id'>;

type TypeOfDocumentFormGroupContent = {
  id: FormControl<ITypeOfDocument['id'] | NewTypeOfDocument['id']>;
  name: FormControl<ITypeOfDocument['name']>;
};

export type TypeOfDocumentFormGroup = FormGroup<TypeOfDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeOfDocumentFormService {
  createTypeOfDocumentFormGroup(typeOfDocument?: TypeOfDocumentFormGroupInput): TypeOfDocumentFormGroup {
    const typeOfDocumentRawValue = {
      ...this.getFormDefaults(),
      ...(typeOfDocument ?? { id: null }),
    };

    return new FormGroup<TypeOfDocumentFormGroupContent>({
      id: new FormControl(
        { value: typeOfDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(typeOfDocumentRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeOfDocument(form: TypeOfDocumentFormGroup): ITypeOfDocument | NewTypeOfDocument {
    return form.getRawValue();
  }

  resetForm(form: TypeOfDocumentFormGroup, typeOfDocument: TypeOfDocumentFormGroupInput): void {
    const typeOfDocumentRawValue = { ...this.getFormDefaults(), ...typeOfDocument };
    form.reset({
      ...typeOfDocumentRawValue,
      id: { value: typeOfDocumentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TypeOfDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
