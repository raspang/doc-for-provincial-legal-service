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
import { RequestedActionDeleteDialog } from '../delete/requested-action-delete-dialog';
import { IRequestedAction } from '../requested-action.model';
import { RequestedActionService } from '../service/requested-action.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-requested-action',
  templateUrl: './requested-action.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective],
})
export class RequestedAction implements OnInit {
  subscription: Subscription | null = null;
  readonly requestedActions = signal<IRequestedAction[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly requestedActionService = inject(RequestedActionService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.requestedActionService.requestedActionsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.requestedActions.set(this.fillComponentAttributesFromResponseBody([...this.requestedActionService.requestedActions()]));
    });
  }

  trackId = (item: IRequestedAction): number => this.requestedActionService.getRequestedActionIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.requestedActions().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(requestedAction: IRequestedAction): void {
    const modalRef = this.modalService.open(RequestedActionDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.requestedAction = requestedAction;
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

  protected refineData(data: IRequestedAction[]): IRequestedAction[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IRequestedAction[]): IRequestedAction[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.requestedActionService.requestedActionsParams.set(queryObject);
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
