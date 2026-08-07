import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { ITypeOfDocument } from '../type-of-document.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-type-of-document-detail',
  templateUrl: './type-of-document-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class TypeOfDocumentDetail {
  readonly typeOfDocument = input<ITypeOfDocument | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
