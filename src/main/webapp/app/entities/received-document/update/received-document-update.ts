import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IDocumentStatus } from 'app/entities/document-status/document-status.model';
import { DocumentStatusService } from 'app/entities/document-status/service/document-status.service';
import { IOffice } from 'app/entities/office/office.model';
import { OfficeService } from 'app/entities/office/service/office.service';
import { IRequestedAction } from 'app/entities/requested-action/requested-action.model';
import { RequestedActionService } from 'app/entities/requested-action/service/requested-action.service';
import { TypeOfDocumentService } from 'app/entities/type-of-document/service/type-of-document.service';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';
import { AlertError } from 'app/shared/alert/alert-error';

import { IReceivedDocument } from '../received-document.model';

import { ReceivedDocumentService } from '../service/received-document.service';
import { ReceivedDocumentFormService, ReceivedDocumentFormGroup } from './received-document-form.service';
import { IResponsiblePerson } from 'app/entities/responsible-person/responsible-person.model';
import { ResponsiblePersonService } from 'app/entities/responsible-person/service/responsible-person.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-received-document-update',
  templateUrl: './received-document-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ReceivedDocumentUpdate implements OnInit {
  readonly isSaving = signal(false);
  receivedDocument: IReceivedDocument | null = null;

  requestedActionsSharedCollection = signal<IRequestedAction[]>([]);
  typeOfDocumentsSharedCollection = signal<ITypeOfDocument[]>([]);
  officesSharedCollection = signal<IOffice[]>([]);
  responsiblePeopleSharedCollection = signal<IResponsiblePerson[]>([]);
  documentStatusesSharedCollection = signal<IDocumentStatus[]>([]);
  transactionTypesSharedCollection = signal<ITransactionType[]>([]);

  protected receivedDocumentService = inject(ReceivedDocumentService);
  protected receivedDocumentFormService = inject(ReceivedDocumentFormService);
  protected requestedActionService = inject(RequestedActionService);
  protected typeOfDocumentService = inject(TypeOfDocumentService);
  protected officeService = inject(OfficeService);
  protected responsiblePersonService = inject(ResponsiblePersonService);
  protected documentStatusService = inject(DocumentStatusService);
  protected transactionTypeService = inject(TransactionTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReceivedDocumentFormGroup = this.receivedDocumentFormService.createReceivedDocumentFormGroup();

  compareRequestedAction = (o1: IRequestedAction | null, o2: IRequestedAction | null): boolean =>
    this.requestedActionService.compareRequestedAction(o1, o2);

  compareTypeOfDocument = (o1: ITypeOfDocument | null, o2: ITypeOfDocument | null): boolean =>
    this.typeOfDocumentService.compareTypeOfDocument(o1, o2);

  compareOffice = (o1: IOffice | null, o2: IOffice | null): boolean => this.officeService.compareOffice(o1, o2);

  compareResponsiblePerson = (o1: IResponsiblePerson | null, o2: IResponsiblePerson | null): boolean =>
    this.responsiblePersonService.compareResponsiblePerson(o1, o2);

  compareDocumentStatus = (o1: IDocumentStatus | null, o2: IDocumentStatus | null): boolean =>
    this.documentStatusService.compareDocumentStatus(o1, o2);

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receivedDocument }) => {
      this.receivedDocument = receivedDocument;
      if (receivedDocument) {
        this.updateForm(receivedDocument);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const receivedDocument = this.receivedDocumentFormService.getReceivedDocument(this.editForm);
    if (receivedDocument.id === null) {
      this.subscribeToSaveResponse(this.receivedDocumentService.create(receivedDocument));
    } else {
      this.subscribeToSaveResponse(this.receivedDocumentService.update(receivedDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IReceivedDocument | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(receivedDocument: IReceivedDocument): void {
    this.receivedDocument = receivedDocument;
    this.receivedDocumentFormService.resetForm(this.editForm, receivedDocument);

    this.requestedActionsSharedCollection.update(requestedActions =>
      this.requestedActionService.addRequestedActionToCollectionIfMissing<IRequestedAction>(
        requestedActions,
        receivedDocument.requestedAction,
      ),
    );
    this.typeOfDocumentsSharedCollection.update(typeOfDocuments =>
      this.typeOfDocumentService.addTypeOfDocumentToCollectionIfMissing<ITypeOfDocument>(typeOfDocuments, receivedDocument.typeOfDocument),
    );
    this.officesSharedCollection.update(offices =>
      this.officeService.addOfficeToCollectionIfMissing<IOffice>(offices, receivedDocument.office),
    );
    this.responsiblePeopleSharedCollection.update(responsiblePeople =>
      this.responsiblePersonService.addResponsiblePersonToCollectionIfMissing<IResponsiblePerson>(
        responsiblePeople,
        receivedDocument.responsiblePerson,
      ),
    );
    this.documentStatusesSharedCollection.update(documentStatuses =>
      this.documentStatusService.addDocumentStatusToCollectionIfMissing<IDocumentStatus>(documentStatuses, receivedDocument.documentStatus),
    );
    this.transactionTypesSharedCollection.update(transactionTypes =>
      this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
        transactionTypes,
        receivedDocument.transactionType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.requestedActionService
      .query()
      .pipe(map((res: HttpResponse<IRequestedAction[]>) => res.body ?? []))
      .pipe(
        map((requestedActions: IRequestedAction[]) =>
          this.requestedActionService.addRequestedActionToCollectionIfMissing<IRequestedAction>(
            requestedActions,
            this.receivedDocument?.requestedAction,
          ),
        ),
      )
      .subscribe((requestedActions: IRequestedAction[]) => this.requestedActionsSharedCollection.set(requestedActions));

    this.typeOfDocumentService
      .query()
      .pipe(map((res: HttpResponse<ITypeOfDocument[]>) => res.body ?? []))
      .pipe(
        map((typeOfDocuments: ITypeOfDocument[]) =>
          this.typeOfDocumentService.addTypeOfDocumentToCollectionIfMissing<ITypeOfDocument>(
            typeOfDocuments,
            this.receivedDocument?.typeOfDocument,
          ),
        ),
      )
      .subscribe((typeOfDocuments: ITypeOfDocument[]) => this.typeOfDocumentsSharedCollection.set(typeOfDocuments));

    this.officeService
      .query()
      .pipe(map((res: HttpResponse<IOffice[]>) => res.body ?? []))
      .pipe(map((offices: IOffice[]) => this.officeService.addOfficeToCollectionIfMissing<IOffice>(offices, this.receivedDocument?.office)))
      .subscribe((offices: IOffice[]) => this.officesSharedCollection.set(offices));

    this.responsiblePersonService
      .query()
      .pipe(map((res: HttpResponse<IResponsiblePerson[]>) => res.body ?? []))
      .pipe(
        map((responsiblePeople: IResponsiblePerson[]) =>
          this.responsiblePersonService.addResponsiblePersonToCollectionIfMissing<IResponsiblePerson>(
            responsiblePeople,
            this.receivedDocument?.responsiblePerson,
          ),
        ),
      )
      .subscribe((responsiblePeople: IResponsiblePerson[]) => this.responsiblePeopleSharedCollection.set(responsiblePeople));

    this.documentStatusService
      .query()
      .pipe(map((res: HttpResponse<IDocumentStatus[]>) => res.body ?? []))
      .pipe(
        map((documentStatuses: IDocumentStatus[]) =>
          this.documentStatusService.addDocumentStatusToCollectionIfMissing<IDocumentStatus>(
            documentStatuses,
            this.receivedDocument?.documentStatus,
          ),
        ),
      )
      .subscribe((documentStatuses: IDocumentStatus[]) => this.documentStatusesSharedCollection.set(documentStatuses));

    this.transactionTypeService
      .query()
      .pipe(map((res: HttpResponse<ITransactionType[]>) => res.body ?? []))
      .pipe(
        map((transactionTypes: ITransactionType[]) =>
          this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
            transactionTypes,
            this.receivedDocument?.transactionType,
          ),
        ),
      )
      .subscribe((transactionTypes: ITransactionType[]) => this.transactionTypesSharedCollection.set(transactionTypes));
  }
}
