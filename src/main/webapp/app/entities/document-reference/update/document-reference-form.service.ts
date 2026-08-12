import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentReference, NewDocumentReference } from '../document-reference.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentReference for edit and NewDocumentReferenceFormGroupInput for create.
 */
type DocumentReferenceFormGroupInput = IDocumentReference | PartialWithRequiredKeyOf<NewDocumentReference>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentReference | NewDocumentReference> = Omit<T, 'date' | 'dateReleased' | 'submittedToSirKing'> & {
  date?: string | null;
  dateReleased?: string | null;
  submittedToSirKing?: string | null;
};

type DocumentReferenceFormRawValue = FormValueOf<IDocumentReference>;

type NewDocumentReferenceFormRawValue = FormValueOf<NewDocumentReference>;

type DocumentReferenceFormDefaults = Pick<NewDocumentReference, 'id' | 'date' | 'dateReleased' | 'submittedToSirKing'>;

type DocumentReferenceFormGroupContent = {
  id: FormControl<DocumentReferenceFormRawValue['id'] | NewDocumentReference['id']>;
  date: FormControl<DocumentReferenceFormRawValue['date']>;
  referenceNo: FormControl<DocumentReferenceFormRawValue['referenceNo']>;
  documentTitle: FormControl<DocumentReferenceFormRawValue['documentTitle']>;
  author: FormControl<DocumentReferenceFormRawValue['author']>;
  dateReleased: FormControl<DocumentReferenceFormRawValue['dateReleased']>;
  submittedToSirKing: FormControl<DocumentReferenceFormRawValue['submittedToSirKing']>;
  remarks: FormControl<DocumentReferenceFormRawValue['remarks']>;
  typeOfDocument: FormControl<DocumentReferenceFormRawValue['typeOfDocument']>;
};

export type DocumentReferenceFormGroup = FormGroup<DocumentReferenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentReferenceFormService {
  createDocumentReferenceFormGroup(documentReference?: DocumentReferenceFormGroupInput): DocumentReferenceFormGroup {
    const documentReferenceRawValue = this.convertDocumentReferenceToDocumentReferenceRawValue({
      ...this.getFormDefaults(),
      ...(documentReference ?? { id: null }),
    });

    return new FormGroup<DocumentReferenceFormGroupContent>({
      id: new FormControl(
        { value: documentReferenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(documentReferenceRawValue.date, {
        validators: [Validators.required],
      }),
      referenceNo: new FormControl(documentReferenceRawValue.referenceNo),
      documentTitle: new FormControl(documentReferenceRawValue.documentTitle, {
        validators: [Validators.required],
      }),
      author: new FormControl(documentReferenceRawValue.author),
      dateReleased: new FormControl(documentReferenceRawValue.dateReleased),
      submittedToSirKing: new FormControl(documentReferenceRawValue.submittedToSirKing),
      remarks: new FormControl(documentReferenceRawValue.remarks),
      typeOfDocument: new FormControl(documentReferenceRawValue.typeOfDocument, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentReference(form: DocumentReferenceFormGroup): IDocumentReference | NewDocumentReference {
    return this.convertDocumentReferenceRawValueToDocumentReference(form.getRawValue());
  }

  resetForm(form: DocumentReferenceFormGroup, documentReference: DocumentReferenceFormGroupInput): void {
    const documentReferenceRawValue = this.convertDocumentReferenceToDocumentReferenceRawValue({
      ...this.getFormDefaults(),
      ...documentReference,
    });
    form.reset({
      ...documentReferenceRawValue,
      id: { value: documentReferenceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentReferenceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      // dateReleased: currentTime,
      // submittedToSirKing: currentTime,
    };
  }

  private convertDocumentReferenceRawValueToDocumentReference(
    rawDocumentReference: DocumentReferenceFormRawValue | NewDocumentReferenceFormRawValue,
  ): IDocumentReference | NewDocumentReference {
    return {
      ...rawDocumentReference,
      date: dayjs(rawDocumentReference.date, DATE_TIME_FORMAT),
      dateReleased: dayjs(rawDocumentReference.dateReleased, DATE_TIME_FORMAT),
      submittedToSirKing: dayjs(rawDocumentReference.submittedToSirKing, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentReferenceToDocumentReferenceRawValue(
    documentReference: IDocumentReference | (Partial<NewDocumentReference> & DocumentReferenceFormDefaults),
  ): DocumentReferenceFormRawValue | PartialWithRequiredKeyOf<NewDocumentReferenceFormRawValue> {
    return {
      ...documentReference,
      date: documentReference.date ? documentReference.date.format(DATE_TIME_FORMAT) : undefined,
      dateReleased: documentReference.dateReleased ? documentReference.dateReleased.format(DATE_TIME_FORMAT) : undefined,
      submittedToSirKing: documentReference.submittedToSirKing ? documentReference.submittedToSirKing.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
