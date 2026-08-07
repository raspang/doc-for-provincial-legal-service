import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { IDocumentReference } from '../document-reference.model';
import { DocumentReferenceService } from '../service/document-reference.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './document-reference-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class DocumentReferenceDeleteDialog {
  documentReference?: IDocumentReference;

  protected readonly documentReferenceService = inject(DocumentReferenceService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentReferenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
