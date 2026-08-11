import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { ITransactionType } from '../transaction-type.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-transaction-type-detail',
  templateUrl: './transaction-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class TransactionTypeDetail {
  readonly transactionType = input<ITransactionType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
