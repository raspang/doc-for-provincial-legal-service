import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { IDocumentStatus } from '../document-status.model';
import { DocumentStatusService } from '../service/document-status.service';

import { DocumentStatusFormGroup, DocumentStatusFormService } from './document-status-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-document-status-update',
  templateUrl: './document-status-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DocumentStatusUpdate implements OnInit {
  readonly isSaving = signal(false);
  documentStatus: IDocumentStatus | null = null;

  protected documentStatusService = inject(DocumentStatusService);
  protected documentStatusFormService = inject(DocumentStatusFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentStatusFormGroup = this.documentStatusFormService.createDocumentStatusFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentStatus }) => {
      this.documentStatus = documentStatus;
      if (documentStatus) {
        this.updateForm(documentStatus);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const documentStatus = this.documentStatusFormService.getDocumentStatus(this.editForm);
    if (documentStatus.id === null) {
      this.subscribeToSaveResponse(this.documentStatusService.create(documentStatus));
    } else {
      this.subscribeToSaveResponse(this.documentStatusService.update(documentStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDocumentStatus | null>): void {
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

  protected updateForm(documentStatus: IDocumentStatus): void {
    this.documentStatus = documentStatus;
    this.documentStatusFormService.resetForm(this.editForm, documentStatus);
  }
}
