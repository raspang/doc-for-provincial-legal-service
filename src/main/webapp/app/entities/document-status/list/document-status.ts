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
import { DocumentStatusDeleteDialog } from '../delete/document-status-delete-dialog';
import { IDocumentStatus } from '../document-status.model';
import { DocumentStatusService } from '../service/document-status.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-document-status',
  templateUrl: './document-status.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective],
})
export class DocumentStatus implements OnInit {
  subscription: Subscription | null = null;
  readonly documentStatuses = signal<IDocumentStatus[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly documentStatusService = inject(DocumentStatusService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.documentStatusService.documentStatusesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.documentStatuses.set(this.fillComponentAttributesFromResponseBody([...this.documentStatusService.documentStatuses()]));
    });
  }

  trackId = (item: IDocumentStatus): number => this.documentStatusService.getDocumentStatusIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.documentStatuses().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(documentStatus: IDocumentStatus): void {
    const modalRef = this.modalService.open(DocumentStatusDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentStatus = documentStatus;
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

  protected refineData(data: IDocumentStatus[]): IDocumentStatus[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IDocumentStatus[]): IDocumentStatus[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.documentStatusService.documentStatusesParams.set(queryObject);
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
