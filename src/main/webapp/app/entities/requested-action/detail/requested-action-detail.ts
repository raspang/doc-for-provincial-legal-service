import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { IRequestedAction } from '../requested-action.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-requested-action-detail',
  templateUrl: './requested-action-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class RequestedActionDetail {
  readonly requestedAction = input<IRequestedAction | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
