import { HttpHeaders } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { Filter, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ItemCount } from 'app/shared/pagination';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ReceivedDocumentDeleteDialog } from '../delete/received-document-delete-dialog';
import { IReceivedDocument } from '../received-document.model';
import { ReceivedDocumentService } from '../service/received-document.service';
import { TypeOfDocumentService } from 'app/entities/type-of-document/service/type-of-document.service';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';
import dayjs from 'dayjs/esm';
import { ResponsiblePersonService } from 'app/entities/responsible-person/service/responsible-person.service';
import { RequestedActionService } from 'app/entities/requested-action/service/requested-action.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-received-document',
  templateUrl: './received-document.html',
  styles: [
    `
      .status-color {
        color: var(--status-color, inherit);
      }

      .blink {
        animation: received-document-highlight-blink 1.2s ease-in-out infinite;
        font-weight: 700;
        color: #000000;
      }

      @keyframes received-document-highlight-blink {
        0%,
        100% {
          background-color: #fff9c4;
        }
        50% {
          background-color: #ffc107;
        }
      }

      @media (prefers-reduced-motion: reduce) {
        .blink {
          animation: none;
          background-color: #fff9c4;
          color: #000000;
        }
      }
    `,
  ],
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    FormatMediumDatetimePipe,
    Filter,
    NgbPagination,
    ItemCount,
  ],
})
export class ReceivedDocument implements OnInit {
  subscription: Subscription | null = null;
  readonly receivedDocuments = signal<IReceivedDocument[]>([]);

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();
  dateFrom?: string;
  dateTo?: string;
  documentTitle?: string;
  typeOfDocumentId: number | null = null;
  responsiblePersonId: number | null = null;
  requestedActionId: number | null = null;

  typeOfDocuments = signal<ITypeOfDocument[]>([]);
  responsiblePersons = signal<ITypeOfDocument[]>([]);
  requestedActions = signal<ITypeOfDocument[]>([]);

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly receivedDocumentService = inject(ReceivedDocumentService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.receivedDocumentService.receivedDocumentsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);
  protected readonly typeOfDocumentService = inject(TypeOfDocumentService);
  protected readonly responsiblePersonService = inject(ResponsiblePersonService);
  protected readonly requestedActionService = inject(RequestedActionService);

  constructor() {
    effect(() => {
      const headers = this.receivedDocumentService.receivedDocumentsResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.receivedDocuments.set(this.fillComponentAttributesFromResponseBody([...this.receivedDocumentService.receivedDocuments()]));
    });

    effect(() => {
      const filterOptions = this.filterOptions();
      if (filterOptions) {
        untracked(() => {
          // Only watch for filter changes. Other signals should be ignored.
          this.handleNavigation(1, this.sortState(), filterOptions);
        });
      }
    });
  }

  trackId = (item: IReceivedDocument): number => this.receivedDocumentService.getReceivedDocumentIdentifier(item);

  ngOnInit(): void {
    this.loadTypeOfDocuments();
    this.loadResponsiblePersonService();
    this.loadRequestedActions();
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  getDueDate(receivedDocument: IReceivedDocument): dayjs.Dayjs | null {
    if (!receivedDocument.date) {
      return null;
    }

    const targetDays = Number(receivedDocument.transactionType?.targetDays);

    if (Number.isNaN(targetDays)) {
      return null;
    }

    return receivedDocument.date.add(targetDays, 'day');
  }

  getDaysBeforeDue(receivedDocument: IReceivedDocument): number | null {
    const dueDate = this.getDueDate(receivedDocument);

    if (!dueDate) {
      return null;
    }

    const today = dayjs().startOf('day');
    const due = dueDate.startOf('day');

    return due.diff(today, 'day');
  }

  displayDaysBeforeDue(receivedDocument: IReceivedDocument): string {
    if (!receivedDocument.documentStatus?.warning) {
      return '';
    }

    const daysBeforeDue = this.getDaysBeforeDue(receivedDocument);

    if (daysBeforeDue === null) {
      return '';
    }

    return daysBeforeDue.toString();
  }

  getTextColor(background?: string | null): string {
    if (!background) {
      return '#000000';
    }

    let hex = background.replace('#', '').trim();

    if (hex.length === 3) {
      hex = hex
        .split('')
        .map(char => char + char)
        .join('');
    }

    if (hex.length !== 6) {
      return '#000000';
    }

    const r = parseInt(hex.substring(0, 2), 16);
    const g = parseInt(hex.substring(2, 4), 16);
    const b = parseInt(hex.substring(4, 6), 16);

    if (Number.isNaN(r) || Number.isNaN(g) || Number.isNaN(b)) {
      return '#000000';
    }

    const brightness = (r * 299 + g * 587 + b * 114) / 1000;

    return brightness > 150 ? '#000000' : '#ffffff';
  }

  isDaysBeforeDueBlink(receivedDocument: IReceivedDocument): boolean {
    const days = this.getDaysBeforeDue(receivedDocument);

    return days !== null && days <= 0;
  }

  delete(receivedDocument: IReceivedDocument): void {
    const modalRef = this.modalService.open(ReceivedDocumentDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.receivedDocument = receivedDocument;
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
    this.handleNavigation(this.page(), event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  applyFilter(): void {
    this.filters.clear();

    if (this.documentTitle?.trim()) {
      this.filters.addFilter('documentTitle.contains', this.documentTitle.trim());
    }

    const dateFromInstant = this.toInstant(this.dateFrom);
    if (dateFromInstant) {
      this.filters.addFilter('date.greaterThanOrEqual', dateFromInstant);
    }

    const dateToInstant = this.toInstant(this.dateTo, true);
    if (dateToInstant) {
      this.filters.addFilter('date.lessThanOrEqual', dateToInstant);
    }

    if (this.typeOfDocumentId) {
      this.filters.addFilter('typeOfDocumentId.equals', this.typeOfDocumentId.toString());
    }

    if (this.responsiblePersonId) {
      this.filters.addFilter('responsiblePersonId.equals', this.responsiblePersonId.toString());
    }

    if (this.requestedActionId) {
      this.filters.addFilter('requestedActionId.equals', this.requestedActionId.toString());
    }
  }

  clearFilter(): void {
    this.documentTitle = undefined;
    this.typeOfDocumentId = null;
    this.dateFrom = undefined;
    this.dateTo = undefined;
    this.filters.clear();
  }

  protected loadTypeOfDocuments(): void {
    this.typeOfDocumentService.query({ page: 0, size: 1000, sort: ['name,asc'] }).subscribe({
      next: res => {
        this.typeOfDocuments.set(res.body ?? []);
      },
      error: () => {
        this.typeOfDocuments.set([]);
      },
    });
  }

  protected loadResponsiblePersonService(): void {
    this.responsiblePersonService.query({ page: 0, size: 1000, sort: ['name,asc'] }).subscribe({
      next: res => {
        this.responsiblePersons.set(res.body ?? []);
      },
      error: () => {
        this.responsiblePersons.set([]);
      },
    });
  }
  protected loadRequestedActions(): void {
    this.requestedActionService.query({ page: 0, size: 1000, sort: ['name,asc'] }).subscribe({
      next: res => {
        this.requestedActions.set(res.body ?? []);
      },
      error: () => {
        this.requestedActions.set([]);
      },
    });
  }

  protected toInstant(value: string | undefined, endOfRange = false): string | undefined {
    if (!value) {
      return undefined;
    }

    if (endOfRange) {
      return dayjs(value).endOf('minute').toISOString();
    }

    return dayjs(value).startOf('minute').toISOString();
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected fillComponentAttributesFromResponseBody(data: IReceivedDocument[]): IReceivedDocument[] {
    return data;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
  }

  protected queryBackend(): void {
    const pageToLoad: number = this.page();
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage(),
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    for (const filterOption of this.filters.filterOptions) {
      queryObject[filterOption.name] = filterOption.values;
    }
    this.receivedDocumentService.receivedDocumentsParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(sortState),
    };

    if (filterOptions) {
      for (const filterOption of filterOptions) {
        queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
      }
    }

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
