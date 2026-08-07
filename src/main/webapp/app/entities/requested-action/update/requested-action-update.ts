import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { IRequestedAction } from '../requested-action.model';
import { RequestedActionService } from '../service/requested-action.service';

import { RequestedActionFormGroup, RequestedActionFormService } from './requested-action-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-requested-action-update',
  templateUrl: './requested-action-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RequestedActionUpdate implements OnInit {
  readonly isSaving = signal(false);
  requestedAction: IRequestedAction | null = null;

  protected requestedActionService = inject(RequestedActionService);
  protected requestedActionFormService = inject(RequestedActionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RequestedActionFormGroup = this.requestedActionFormService.createRequestedActionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requestedAction }) => {
      this.requestedAction = requestedAction;
      if (requestedAction) {
        this.updateForm(requestedAction);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const requestedAction = this.requestedActionFormService.getRequestedAction(this.editForm);
    if (requestedAction.id === null) {
      this.subscribeToSaveResponse(this.requestedActionService.create(requestedAction));
    } else {
      this.subscribeToSaveResponse(this.requestedActionService.update(requestedAction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRequestedAction | null>): void {
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

  protected updateForm(requestedAction: IRequestedAction): void {
    this.requestedAction = requestedAction;
    this.requestedActionFormService.resetForm(this.editForm, requestedAction);
  }
}
