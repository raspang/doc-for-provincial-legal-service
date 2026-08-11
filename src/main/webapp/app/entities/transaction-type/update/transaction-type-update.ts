import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

import { TransactionTypeFormGroup, TransactionTypeFormService } from './transaction-type-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-transaction-type-update',
  templateUrl: './transaction-type-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TransactionTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  transactionType: ITransactionType | null = null;

  protected transactionTypeService = inject(TransactionTypeService);
  protected transactionTypeFormService = inject(TransactionTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionTypeFormGroup = this.transactionTypeFormService.createTransactionTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionType }) => {
      this.transactionType = transactionType;
      if (transactionType) {
        this.updateForm(transactionType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const transactionType = this.transactionTypeFormService.getTransactionType(this.editForm);
    if (transactionType.id === null) {
      this.subscribeToSaveResponse(this.transactionTypeService.create(transactionType));
    } else {
      this.subscribeToSaveResponse(this.transactionTypeService.update(transactionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITransactionType | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(transactionType: ITransactionType): void {
    this.transactionType = transactionType;
    this.transactionTypeFormService.resetForm(this.editForm, transactionType);
  }
}
