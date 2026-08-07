import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { IResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

import { ResponsiblePersonFormGroup, ResponsiblePersonFormService } from './responsible-person-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-responsible-person-update',
  templateUrl: './responsible-person-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ResponsiblePersonUpdate implements OnInit {
  readonly isSaving = signal(false);
  responsiblePerson: IResponsiblePerson | null = null;

  protected responsiblePersonService = inject(ResponsiblePersonService);
  protected responsiblePersonFormService = inject(ResponsiblePersonFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResponsiblePersonFormGroup = this.responsiblePersonFormService.createResponsiblePersonFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsiblePerson }) => {
      this.responsiblePerson = responsiblePerson;
      if (responsiblePerson) {
        this.updateForm(responsiblePerson);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const responsiblePerson = this.responsiblePersonFormService.getResponsiblePerson(this.editForm);
    if (responsiblePerson.id === null) {
      this.subscribeToSaveResponse(this.responsiblePersonService.create(responsiblePerson));
    } else {
      this.subscribeToSaveResponse(this.responsiblePersonService.update(responsiblePerson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IResponsiblePerson | null>): void {
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

  protected updateForm(responsiblePerson: IResponsiblePerson): void {
    this.responsiblePerson = responsiblePerson;
    this.responsiblePersonFormService.resetForm(this.editForm, responsiblePerson);
  }
}
