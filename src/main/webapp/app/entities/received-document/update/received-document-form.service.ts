import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReceivedDocument, NewReceivedDocument } from '../received-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReceivedDocument for edit and NewReceivedDocumentFormGroupInput for create.
 */
type ReceivedDocumentFormGroupInput = IReceivedDocument | PartialWithRequiredKeyOf<NewReceivedDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReceivedDocument | NewReceivedDocument> = Omit<T, 'date' | 'dateReleased'> & {
  date?: string | null;
  dateReleased?: string | null;
};

type ReceivedDocumentFormRawValue = FormValueOf<IReceivedDocument>;

type NewReceivedDocumentFormRawValue = FormValueOf<NewReceivedDocument>;

type ReceivedDocumentFormDefaults = Pick<NewReceivedDocument, 'id' | 'date' | 'dateReleased'>;

type ReceivedDocumentFormGroupContent = {
  id: FormControl<ReceivedDocumentFormRawValue['id'] | NewReceivedDocument['id']>;
  date: FormControl<ReceivedDocumentFormRawValue['date']>;
  documentTitle: FormControl<ReceivedDocumentFormRawValue['documentTitle']>;
  dateReleased: FormControl<ReceivedDocumentFormRawValue['dateReleased']>;
  remarks: FormControl<ReceivedDocumentFormRawValue['remarks']>;
  requestedAction: FormControl<ReceivedDocumentFormRawValue['requestedAction']>;
  typeOfDocument: FormControl<ReceivedDocumentFormRawValue['typeOfDocument']>;
  office: FormControl<ReceivedDocumentFormRawValue['office']>;
  responsiblePerson: FormControl<ReceivedDocumentFormRawValue['responsiblePerson']>;
  documentStatus: FormControl<ReceivedDocumentFormRawValue['documentStatus']>;
  transactionType: FormControl<ReceivedDocumentFormRawValue['transactionType']>;
};

export type ReceivedDocumentFormGroup = FormGroup<ReceivedDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReceivedDocumentFormService {
  createReceivedDocumentFormGroup(receivedDocument?: ReceivedDocumentFormGroupInput): ReceivedDocumentFormGroup {
    const receivedDocumentRawValue = this.convertReceivedDocumentToReceivedDocumentRawValue({
      ...this.getFormDefaults(),
      ...(receivedDocument ?? { id: null }),
    });

    return new FormGroup<ReceivedDocumentFormGroupContent>({
      id: new FormControl(
        { value: receivedDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(receivedDocumentRawValue.date, {
        validators: [Validators.required],
      }),
      documentTitle: new FormControl(receivedDocumentRawValue.documentTitle, {
        validators: [Validators.required],
      }),
      dateReleased: new FormControl(receivedDocumentRawValue.dateReleased),
      remarks: new FormControl(receivedDocumentRawValue.remarks),
      requestedAction: new FormControl(receivedDocumentRawValue.requestedAction),
      typeOfDocument: new FormControl(receivedDocumentRawValue.typeOfDocument, {
        validators: [Validators.required],
      }),
      office: new FormControl(receivedDocumentRawValue.office),
      responsiblePerson: new FormControl(receivedDocumentRawValue.responsiblePerson),
      documentStatus: new FormControl(receivedDocumentRawValue.documentStatus, {
        validators: [Validators.required],
      }),
      transactionType: new FormControl(receivedDocumentRawValue.transactionType),
    });
  }

  getReceivedDocument(form: ReceivedDocumentFormGroup): IReceivedDocument | NewReceivedDocument {
    return this.convertReceivedDocumentRawValueToReceivedDocument(form.getRawValue());
  }

  resetForm(form: ReceivedDocumentFormGroup, receivedDocument: ReceivedDocumentFormGroupInput): void {
    const receivedDocumentRawValue = this.convertReceivedDocumentToReceivedDocumentRawValue({
      ...this.getFormDefaults(),
      ...receivedDocument,
    });
    form.reset({
      ...receivedDocumentRawValue,
      id: { value: receivedDocumentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ReceivedDocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      dateReleased: currentTime,
    };
  }

  private convertReceivedDocumentRawValueToReceivedDocument(
    rawReceivedDocument: ReceivedDocumentFormRawValue | NewReceivedDocumentFormRawValue,
  ): IReceivedDocument | NewReceivedDocument {
    return {
      ...rawReceivedDocument,
      date: dayjs(rawReceivedDocument.date, DATE_TIME_FORMAT),
      dateReleased: dayjs(rawReceivedDocument.dateReleased, DATE_TIME_FORMAT),
    };
  }

  private convertReceivedDocumentToReceivedDocumentRawValue(
    receivedDocument: IReceivedDocument | (Partial<NewReceivedDocument> & ReceivedDocumentFormDefaults),
  ): ReceivedDocumentFormRawValue | PartialWithRequiredKeyOf<NewReceivedDocumentFormRawValue> {
    return {
      ...receivedDocument,
      date: receivedDocument.date ? receivedDocument.date.format(DATE_TIME_FORMAT) : undefined,
      dateReleased: receivedDocument.dateReleased ? receivedDocument.dateReleased.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
