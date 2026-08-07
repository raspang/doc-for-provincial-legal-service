import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { IResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './responsible-person-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class ResponsiblePersonDeleteDialog {
  responsiblePerson?: IResponsiblePerson;

  protected readonly responsiblePersonService = inject(ResponsiblePersonService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.responsiblePersonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
