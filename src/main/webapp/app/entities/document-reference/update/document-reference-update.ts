import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { TypeOfDocumentService } from 'app/entities/type-of-document/service/type-of-document.service';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { IDocumentReference } from '../document-reference.model';
import { DocumentReferenceService } from '../service/document-reference.service';

import { DocumentReferenceFormGroup, DocumentReferenceFormService } from './document-reference-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-document-reference-update',
  templateUrl: './document-reference-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DocumentReferenceUpdate implements OnInit {
  readonly isSaving = signal(false);
  documentReference: IDocumentReference | null = null;

  typeOfDocumentsSharedCollection = signal<ITypeOfDocument[]>([]);

  protected documentReferenceService = inject(DocumentReferenceService);
  protected documentReferenceFormService = inject(DocumentReferenceFormService);
  protected typeOfDocumentService = inject(TypeOfDocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentReferenceFormGroup = this.documentReferenceFormService.createDocumentReferenceFormGroup();

  compareTypeOfDocument = (o1: ITypeOfDocument | null, o2: ITypeOfDocument | null): boolean =>
    this.typeOfDocumentService.compareTypeOfDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentReference }) => {
      this.documentReference = documentReference;
      if (documentReference) {
        this.updateForm(documentReference);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const documentReference = this.documentReferenceFormService.getDocumentReference(this.editForm);

    if (documentReference.id === null) {
      this.subscribeToSaveResponse(this.documentReferenceService.create(documentReference));
    } else {
      this.subscribeToSaveResponse(this.documentReferenceService.update(documentReference));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDocumentReference | null>): void {
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

  protected updateForm(documentReference: IDocumentReference): void {
    this.documentReference = documentReference;
    this.documentReferenceFormService.resetForm(this.editForm, documentReference);

    this.typeOfDocumentsSharedCollection.update(typeOfDocuments =>
      this.typeOfDocumentService.addTypeOfDocumentToCollectionIfMissing<ITypeOfDocument>(typeOfDocuments, documentReference.typeOfDocument),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeOfDocumentService
      .query()
      .pipe(map((res: HttpResponse<ITypeOfDocument[]>) => res.body ?? []))
      .pipe(
        map((typeOfDocuments: ITypeOfDocument[]) =>
          this.typeOfDocumentService.addTypeOfDocumentToCollectionIfMissing<ITypeOfDocument>(
            typeOfDocuments,
            this.documentReference?.typeOfDocument,
          ),
        ),
      )
      .subscribe((typeOfDocuments: ITypeOfDocument[]) => this.typeOfDocumentsSharedCollection.set(typeOfDocuments));
  }
}
