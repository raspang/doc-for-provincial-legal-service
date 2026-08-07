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
import { TypeOfDocumentDeleteDialog } from '../delete/type-of-document-delete-dialog';
import { TypeOfDocumentService } from '../service/type-of-document.service';
import { ITypeOfDocument } from '../type-of-document.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-type-of-document',
  templateUrl: './type-of-document.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective],
})
export class TypeOfDocument implements OnInit {
  subscription: Subscription | null = null;
  readonly typeOfDocuments = signal<ITypeOfDocument[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly typeOfDocumentService = inject(TypeOfDocumentService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.typeOfDocumentService.typeOfDocumentsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.typeOfDocuments.set(this.fillComponentAttributesFromResponseBody([...this.typeOfDocumentService.typeOfDocuments()]));
    });
  }

  trackId = (item: ITypeOfDocument): number => this.typeOfDocumentService.getTypeOfDocumentIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.typeOfDocuments().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(typeOfDocument: ITypeOfDocument): void {
    const modalRef = this.modalService.open(TypeOfDocumentDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.typeOfDocument = typeOfDocument;
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

  protected refineData(data: ITypeOfDocument[]): ITypeOfDocument[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: ITypeOfDocument[]): ITypeOfDocument[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.typeOfDocumentService.typeOfDocumentsParams.set(queryObject);
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
