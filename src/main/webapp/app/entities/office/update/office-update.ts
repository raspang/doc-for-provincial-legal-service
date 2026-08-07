import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { IOffice } from '../office.model';
import { OfficeService } from '../service/office.service';

import { OfficeFormGroup, OfficeFormService } from './office-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-office-update',
  templateUrl: './office-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OfficeUpdate implements OnInit {
  readonly isSaving = signal(false);
  office: IOffice | null = null;

  protected officeService = inject(OfficeService);
  protected officeFormService = inject(OfficeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OfficeFormGroup = this.officeFormService.createOfficeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ office }) => {
      this.office = office;
      if (office) {
        this.updateForm(office);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const office = this.officeFormService.getOffice(this.editForm);
    if (office.id === null) {
      this.subscribeToSaveResponse(this.officeService.create(office));
    } else {
      this.subscribeToSaveResponse(this.officeService.update(office));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOffice | null>): void {
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

  protected updateForm(office: IOffice): void {
    this.office = office;
    this.officeFormService.resetForm(this.editForm, office);
  }
}
