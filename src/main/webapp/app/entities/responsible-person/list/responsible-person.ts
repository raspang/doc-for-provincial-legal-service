import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ResponsiblePersonDeleteDialog } from '../delete/responsible-person-delete-dialog';
import { IResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-responsible-person',
  templateUrl: './responsible-person.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective],
})
export class ResponsiblePerson implements OnInit {
  subscription: Subscription | null = null;
  readonly responsiblePeople = signal<IResponsiblePerson[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly responsiblePersonService = inject(ResponsiblePersonService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.responsiblePersonService.responsiblePeopleResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.responsiblePeople.set(this.fillComponentAttributesFromResponseBody([...this.responsiblePersonService.responsiblePeople()]));
    });
  }

  trackId = (item: IResponsiblePerson): number => this.responsiblePersonService.getResponsiblePersonIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.responsiblePeople().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(responsiblePerson: IResponsiblePerson): void {
    const modalRef = this.modalService.open(ResponsiblePersonDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.responsiblePerson = responsiblePerson;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IResponsiblePerson[]): IResponsiblePerson[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IResponsiblePerson[]): IResponsiblePerson[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.responsiblePersonService.responsiblePeopleParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
