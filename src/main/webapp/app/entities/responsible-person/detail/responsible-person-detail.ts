import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { IResponsiblePerson } from '../responsible-person.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-responsible-person-detail',
  templateUrl: './responsible-person-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class ResponsiblePersonDetail {
  readonly responsiblePerson = input<IResponsiblePerson | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
