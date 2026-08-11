import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './transaction-type-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class TransactionTypeDeleteDialog {
  transactionType?: ITransactionType;

  protected readonly transactionTypeService = inject(TransactionTypeService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
