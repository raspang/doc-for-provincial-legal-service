import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { IOffice } from '../office.model';
import { OfficeService } from '../service/office.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './office-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class OfficeDeleteDialog {
  office?: IOffice;

  protected readonly officeService = inject(OfficeService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.officeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
