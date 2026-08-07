import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { IDocumentStatus } from '../document-status.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-document-status-detail',
  templateUrl: './document-status-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class DocumentStatusDetail {
  readonly documentStatus = input<IDocumentStatus | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
