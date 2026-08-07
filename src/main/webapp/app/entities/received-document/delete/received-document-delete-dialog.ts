import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { IReceivedDocument } from '../received-document.model';
import { ReceivedDocumentService } from '../service/received-document.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './received-document-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class ReceivedDocumentDeleteDialog {
  receivedDocument?: IReceivedDocument;

  protected readonly receivedDocumentService = inject(ReceivedDocumentService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.receivedDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
