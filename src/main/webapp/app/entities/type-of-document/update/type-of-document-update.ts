import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { TypeOfDocumentService } from '../service/type-of-document.service';
import { ITypeOfDocument } from '../type-of-document.model';

import { TypeOfDocumentFormGroup, TypeOfDocumentFormService } from './type-of-document-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-type-of-document-update',
  templateUrl: './type-of-document-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TypeOfDocumentUpdate implements OnInit {
  readonly isSaving = signal(false);
  typeOfDocument: ITypeOfDocument | null = null;

  protected typeOfDocumentService = inject(TypeOfDocumentService);
  protected typeOfDocumentFormService = inject(TypeOfDocumentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeOfDocumentFormGroup = this.typeOfDocumentFormService.createTypeOfDocumentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOfDocument }) => {
      this.typeOfDocument = typeOfDocument;
      if (typeOfDocument) {
        this.updateForm(typeOfDocument);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const typeOfDocument = this.typeOfDocumentFormService.getTypeOfDocument(this.editForm);
    if (typeOfDocument.id === null) {
      this.subscribeToSaveResponse(this.typeOfDocumentService.create(typeOfDocument));
    } else {
      this.subscribeToSaveResponse(this.typeOfDocumentService.update(typeOfDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITypeOfDocument | null>): void {
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

  protected updateForm(typeOfDocument: ITypeOfDocument): void {
    this.typeOfDocument = typeOfDocument;
    this.typeOfDocumentFormService.resetForm(this.editForm, typeOfDocument);
  }
}
